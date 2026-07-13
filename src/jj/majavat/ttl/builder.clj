(ns jj.majavat.ttl.builder
  (:require
            [jj.majavat.error-handler.reporting :as reporting]
            [jj.majavat.parser :as parser]
            [jj.majavat.protocol.renderer.render-target :as renderer]
            [jj.majavat.protocol.builder :as builder]
            [jj.majavat.renderer :refer [->StringRenderer]]
            [jj.majavat.renderer.sanitizer :as sanitizer]
            [jj.majavat.resolver.resource :as rcr])
  (:import (java.util.concurrent ScheduledThreadPoolExecutor TimeUnit)
           ))

(def ^:private default-executor (ScheduledThreadPoolExecutor. 1))

(defrecord TTLBuilder [config]
  builder/Builder
  (build-renderer [_ file-path template-resolver expected-renderer sanitizer error-handler]
    (let [{:keys [ttl
                  type
                  executor
                  environment]
           :or   {ttl      7
                  type     TimeUnit/DAYS
                  executor default-executor}} config
          {:keys [filters sanitizers dictionary]} environment
          template (atom nil)]
      (reset! template (parser/parse file-path template-resolver filters sanitizers dictionary sanitizer))
      (let [ex (or (:executor config) default-executor)]
        (.scheduleAtFixedRate ^ScheduledThreadPoolExecutor
                              ex (fn []
                                   (try
                                     (let [new-template (parser/parse file-path template-resolver filters sanitizers dictionary sanitizer)]
                                       (reset! template new-template))
                                     (catch Exception e
                                       (println "Error reloading template:" (.getMessage e))))) ttl ttl type))

      (fn [context]
        (renderer/render expected-renderer @template context error-handler)))))

(defn build-ttl-renderer
  ([file-path]
   (build-ttl-renderer file-path {}))
  ([file-path {:keys [template-resolver
                      renderer
                      ttl
                      type
                      executor
                      environment
                      sanitizer
                      error-handler]
               :or   {template-resolver (rcr/->ResourceResolver)
                      renderer          (->StringRenderer )
                      ttl               7
                      executor          nil
                      type              TimeUnit/DAYS
                      environment       {}
                      sanitizer         (sanitizer/->None)
                      error-handler     (reporting/->Reporting)}}]

   (builder/build-renderer (TTLBuilder. {:ttl         ttl
                                         :executor    executor
                                         :type        type
                                         :environment environment})
                           file-path
                           template-resolver
                           renderer
                           sanitizer
                           error-handler)))
