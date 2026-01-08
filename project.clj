(defproject org.clojars.jj/majavat-ttl-builder "1.0.0-SNAPSHOT"
  :description "TTL Builder is an extension to majavat that allows scheduling cache reloading."
  :url "https://github.com/ruroru/majavat-ttl-builder"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.4"]
                 [org.clojure/tools.logging "1.3.1"]
                 [org.clojars.jj/majavat "1.13.3"]]

  :deploy-repositories [["clojars" {:url      "https://repo.clojars.org"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass}]]

  :profiles {:test {:global-vars    {*warn-on-reflection* true}
                    :dependencies   [[ch.qos.logback/logback-classic "1.5.24"]
                                     [mock-clj "0.2.1"]]
                    :resource-paths ["test-resources"]}}

  :plugins [[org.clojars.jj/bump "1.0.4"]
            [org.clojars.jj/strict-check "1.1.0"]
            [org.clojars.jj/bump-md "1.1.0"]]

  )
