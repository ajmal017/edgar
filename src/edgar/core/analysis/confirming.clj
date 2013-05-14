(ns edgar.core.analysis.confirming)

(defn on-balance-volume
  "On Balance Volume (OBV) measures buying and selling pressure as a cumulative indicator that i) adds volume on up days and ii) subtracts volume on down days. We'll look for divergences between OBV and price to predict price movements or use OBV to confirm price trends.

   The On Balance Volume (OBV) line is a running total of positive and negative volume. i) A tick's volume is positive when the close is above the prior close. Or ii) a tick's volume is negative when the close is below the prior close.

    If closing price is above prior:
      Current OBV = Previous OBV + Current Volume

    If closing price is below prior:
      Current OBV = Previous OBV  -  Current Volume

    If closing price equals prior:
      Current OBV = Previous OBV (no change)

    ** The first OBV value is the first period's positive/negative volume."
  [latest-tick tick-list]


  ;; accumulate OBV on historical tick-list
  (let [obv-list (reduce (fn [rslt ech]

                           (if-let [prev-obv (:obv (first rslt))]    ;; handling case where first will not have an OBV

                             ;; normal case
                             (let [current-price (if (string? (:last-trade-price (first ech)))
                                                   (read-string (:last-trade-price (first ech)))
                                                   (:last-trade-price (first ech)))
                                   prev-price (if (string? (:last-trade-price (second ech)))
                                                (read-string (:last-trade-price (second ech)))
                                                (:last-trade-price (second ech)))
                                   current-volume (if (string? (:total-volume (first ech)))
                                                    (read-string (:total-volume (first ech)))
                                                    (:total-volume (first ech)))

                                   obv (if (= current-price prev-price)
                                         prev-obv
                                         (if (> current-price prev-price)
                                           (+ prev-obv current-volume)
                                           (- prev-obv current-volume)))
                                   ]

                               (cons {:obv obv
                                      :total-volume (:total-volume (first ech))
                                      :last-trade-price (:last-trade-price (first ech))
                                      :last-trade-time (:last-trade-time (first ech))} rslt))

                             ;; otherwise we seed the list with the first entry
                             (cons {:obv (:total-volume (first ech))
                                    :total-volume (:total-volume (first ech))
                                    :last-trade-price (:last-trade-price (first ech))
                                    :last-trade-time (:last-trade-time (first ech))} rslt)
                             )

                           )
                         '(nil)
                         (->> tick-list (partition 2 1) reverse))
        ]

    ;; calculate OBV for latest tick
    (if latest-tick

      (let [cprice (if (string? (:last-trade-price latest-tick))
                     (read-string (:last-trade-price latest-tick))
                     (:last-trade-price latest-tick))
            pprice (if (string? (:last-trade-price (first obv-list)))
                     (read-string (:last-trade-price (first obv-list)))
                     (:last-trade-price (first obv-list)))
            cvolume (if (string? (:total-volume latest-tick))
                      (read-string (:total-volume latest-tick))
                      (:total-volume latest-tick))
            pobv (:obv (first obv-list))

            cobv (if (= cprice pprice)
                   pobv
                   (if (> cprice pprice)
                     (+ pobv cvolume)
                     (- pobv cvolume)))]

        (cons {:obv cobv
               :total-volume (:total-volume latest-tick)
               :last-trade-price (:last-trade-price latest-tick)
               :last-trade-time (:last-trade-time latest-tick)} obv-list))
      obv-list)
    )
)
