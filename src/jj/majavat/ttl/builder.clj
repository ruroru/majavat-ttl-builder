(ns jj.majavat.ttl.builder
  (:require [jj.majavat.builder :as builder]
            [jj.majavat.parser :as parser]
            [jj.majavat.renderer :as renderer]
            [jj.majavat.renderer :refer [->StringRenderer]]
            [jj.majavat.resolver.resource :as rcr])
  (:import (java.util.concurrent ScheduledThreadPoolExecutor TimeUnit)))

(def ^:private default-executor (ScheduledThreadPoolExecutor. 1))
(def ^:private sanitizer {})

(defrecord TTLBuilder [config]
  builder/Builder
  (build-renderer [_ file-path template-resolver expected-renderer sanitizer]
    (let [{:keys [ttl
                  type
                  executor]
           :or   {ttl      7
                  type     TimeUnit/DAYS
                  executor default-executor}} config
          template (atom nil)]
      (reset! template (parser/parse file-path template-resolver))
      (let [ex (or (:executor config) default-executor)]
        (.scheduleAtFixedRate ^ScheduledThreadPoolExecutor
                              ex (fn []
                                   (try
                                     (let [new-template (parser/parse file-path template-resolver)]
                                       (reset! template new-template))
                                     (catch Exception e
                                       (println "Error reloading template:" (.getMessage e))))) ttl ttl type))

      (fn [context]
        (renderer/render expected-renderer @template context sanitizer)))))

(defn build-ttl-renderer
  ([file-path]
   (build-ttl-renderer file-path {}))
  ([file-path {:keys [template-resolver
                      renderer
                      ttl
                      type
                      executor]
               :or   {template-resolver (rcr/->ResourceResolver)
                      renderer          (->StringRenderer )
                      ttl               7
                      executor          nil
                      type              TimeUnit/DAYS}}]

   (builder/build-renderer (TTLBuilder. {:ttl      ttl
                                         :executor executor
                                         :type     type})
                           file-path
                           template-resolver
                           renderer
                           sanitizer)))
