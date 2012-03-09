(ns server.utils)

(defn clj->js
  "Recursively transforms ClojureScript maps into Javascript objects,
   other ClojureScript colls into JavaScript arrays, and ClojureScript
   keywords into JavaScript strings."
  [x]
  (cond
   (string? x) x
   (keyword? x) (name x)
   (map? x) (.-strobj (reduce (fn [m [k v]]
                                (assoc m (clj->js k) (clj->js v))) {} x))
   (coll? x) (apply array (map clj->js x))
   :else x))

(defn prn-js [json]
  (print (.stringify js/JSON json) "\n"))

(defn clj->json [c]
  (.stringify js/JSON (clj->js c)))

(defn transform-keys [key-map in]
  (reduce
   (fn [m [q-name d-name]]
     
     (if-let [val (in q-name)]
       (if (vector? d-name)
         (let [[d-name transformer] d-name]
           (assoc m d-name (transformer val)))
         (assoc m d-name val))))
   {}
   key-map))

(defn prn [form]
  (print "prn>" (pr-str form) "\n"))