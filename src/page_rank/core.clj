(ns page-rank.core
  (:gen-class))

(def criterio-parada 0.000001)

(def matriz-grafo [[0 1 1]
                   [1 0 1]
                   [0 1 0]])

(defn valor-absoluto [n]
  (if (neg? n)
    (- n)
    n))

(defn parar? [v1 v2]
  (let [vector-resultante (map (fn [e1 e2] (valor-absoluto (- e1 e2))) v1 v2)
        vector-filtrado (filter (fn [element] (< criterio-parada element)) 
                                vector-resultante)]
    (empty? vector-filtrado)))

(defn matriz-estocastica [matriz-page]
  (let [suma-columnas (apply map + matriz-page)]
    (map (fn [elements] 
           (map (fn [e1 e2] (/ e1 e2)) 
                elements suma-columnas)) 
         matriz-page)))

(defn vector-equiprobable [matriz]
  (let [contador-filas (count matriz)]
    (vec (repeat contador-filas (/ 1 contador-filas)))))

(defn multiplicar-matriz-por-vector [matriz vi]
  (reduce (fn [new-vect elements] (conj new-vect (reduce + (map * vi elements)))) [] matriz))

(defn obtener-vector-resultante [matriz v0]
  (loop [v1 v0
         v2 (multiplicar-matriz-por-vector matriz v1)]
    (if (parar? v1 v2)
      v2
      (recur v2 (multiplicar-matriz-por-vector matriz v2)))))

(def matriz-est (matriz-estocastica matriz-grafo))
(def vect-ep (vector-equiprobable matriz-grafo))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [matriz-est (matriz-estocastica matriz-grafo)
        vect-ep (vector-equiprobable matriz-grafo)]
    (println "Page Rank => ")
    (println (obtener-vector-resultante matriz-est  vect-ep))))
