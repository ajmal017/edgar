(ns edgar.core.signal.common)


(defn find-peaks-valleys [options tick-list]

  (let [{input-key :input
         :or {input-key :last-trade-price}} options]

    (reduce (fn [rslt ech]
              (let [fst (if (string? (input-key (first ech)))
                          (read-string (input-key (first ech)))
                          (input-key (first ech)))

                    snd (if (string? (input-key (second ech)))
                          (read-string (input-key (second ech)))
                          (input-key (second ech)))

                    thd (if (string? (input-key (nth ech 2)))
                          (read-string (input-key (nth ech 2)))
                          (input-key (nth ech 2)))

                    valley? (and (and (-> fst nil? not) (-> snd nil? not) (-> thd nil? not))
                                 (> fst snd)
                                 (< snd thd))
                    peak? (and (and (-> fst nil? not) (-> snd nil? not) (-> thd nil? not))
                               (< fst snd)
                               (> snd thd))]

                (if (or valley? peak?)
                  (if peak?
                    (conj rslt (assoc (second ech) :signal :peak))
                    (conj rslt (assoc (second ech) :signal :valley)))
                  rslt)))
            []
            (partition 3 1 tick-list))))


(defn up-market? [period partitioned-list]
  (every? (fn [inp]
            (> (if (string? (:last-trade-price (first inp)))
                 (read-string (:last-trade-price (first inp)))
                 (:last-trade-price (first inp)))
               (if (string? (:last-trade-price (second inp)))
                 (read-string (:last-trade-price (second inp)))
                 (:last-trade-price (second inp)))))
          (take period partitioned-list)))


(defn down-market? [period partitioned-list]
  (every? (fn [inp]
            (< (if (string? (:last-trade-price (first inp)))
                 (read-string (:last-trade-price (first inp)))
                 (:last-trade-price (first inp)))
               (if (string? (:last-trade-price (second inp)))
                 (read-string (:last-trade-price (second inp)))
                 (:last-trade-price (second inp)))))
          (take period partitioned-list)))

(defn divergence-up? [options ech-list price-peaks-valleys macd-peaks-valleys]

  (let [
        first-ech (first ech-list)
        first-price (first price-peaks-valleys)
        first-macd (first macd-peaks-valleys)

        {input-top :input-top
         input-bottom :input-bottom
         :or {input-top :last-trade-price
              input-bottom :last-trade-macd}} options

        price-higher-high? (and (-> (input-top first-ech) nil? not)
                                (-> (input-top first-price) nil? not)
                                (> (input-top first-ech) (input-top first-price)))

        macd-lower-high? (and (-> (input-bottom first-ech) nil? not)
                              (-> (input-bottom first-macd) nil? not)
                              (< (input-bottom first-ech) (input-bottom first-macd)))]

    (and price-higher-high? macd-lower-high?)))

(defn divergence-down? [options ech-list price-peaks-valleys macd-peaks-valleys]

  (let [
        first-ech (first ech-list)
        first-price (first price-peaks-valleys)
        first-macd (first macd-peaks-valleys)

        {input-top :input-top
         input-bottom :input-bottom
         :or {input-top :last-trade-price
              input-bottom :last-trade-macd}} options

        price-lower-high? (and (-> (input-top first-ech) nil? not)
                               (-> (input-top first-price) nil? not)
                               (< (input-top (first ech-list)) (input-top (first price-peaks-valleys))))

        macd-higher-high? (and (-> (input-top first-ech) nil? not)
                               (-> (input-top first-price) nil? not)
                               (> (input-bottom (first ech-list)) (input-bottom (first macd-peaks-valleys))))]

    (and price-lower-high? macd-higher-high?)))
