(ns edgar.core.signal.lagging
  (:require [edgar.core.analysis.lagging :as analysis]))


(defn join-averages
  "Create a list where i) tick-list ii) sma-list and iii) ema-list are overlaid"

  ([tick-window tick-list]

     (let [sma-list (analysis/simple-moving-average nil tick-window tick-list)
           ema-list (analysis/exponential-moving-average nil tick-window tick-list sma-list)]
       (join-averages tick-window tick-list sma-list ema-list)))

  ([tick-window tick-list sma-list ema-list]
     (map (fn [titem sitem eitem]

            ;; 1. ensure that we have the :last-trade-time for simple and exponential items
            ;; 2. ensure that all 3 time items line up
            (if (and (and (not (nil? (:last-trade-time sitem)))
                          (not (nil? (:last-trade-time eitem))))
                     (= (:last-trade-time titem) (:last-trade-time sitem) (:last-trade-time eitem)))

              {:last-trade-time (:last-trade-time titem)
               :last-trade-price (read-string (:last-trade-price titem))
               :last-trade-price-average (:last-trade-price-average sitem)
               :last-trade-price-exponential (:last-trade-price-exponential eitem)}

              nil))

          tick-list
          sma-list
          ema-list)))


(defn moving-averages
  "Takes baseline time series, along with 2 other moving averages.

   Produces a list of signals where the 2nd moving average overlaps (abouve or below) the first.
   By default, this function will produce a Simple Moving Average and an Exponential Moving Average."

  ([tick-window tick-list]

     (let [sma-list (analysis/simple-moving-average nil tick-window tick-list)
           ema-list (analysis/exponential-moving-average nil tick-window tick-list sma-list)]
       (moving-averages tick-window tick-list sma-list ema-list)))

  ([tick-window tick-list sma-list ema-list]

     ;; create a list where i) tick-list ii) sma-list and iii) ema-list are overlaid
     (let [joined-list (join-averages tick-window tick-list sma-list ema-list)
           partitioned-join (partition 2 1 (remove nil? joined-list))
           start-list (into '() (repeat tick-window nil))]


       ;; find time points where ema-list (or second list) crosses over the sma-list (or 1st list)
       (reduce (fn [rslt ech]

                 (let [fst (first ech)
                       snd (second ech)

                       ;; in the first element, has the ema crossed abouve the sma from the second element
                       signal-up (and (< (:last-trade-price-exponential snd) (:last-trade-price-average snd))
                                      (> (:last-trade-price-exponential fst) (:last-trade-price-average snd)))

                       ;; in the first element, has the ema crossed below the sma from the second element
                       signal-down (and (> (:last-trade-price-exponential snd) (:last-trade-price-average snd))
                                        (< (:last-trade-price-exponential fst) (:last-trade-price-average snd)))

                       raw-data fst
                       ]

                   (println (str "... up[" signal-up "] / down[" signal-down "] / first[" (dissoc fst :last-trade-price) "] / second[" (dissoc snd :last-trade-price) "]"))

                   ;; return either i) :up signal, ii) :down signal or iii) nothing, with just the raw data
                   (if signal-up
                     (cons (assoc raw-data :signal :up) rslt)
                     (if signal-down
                       (cons (assoc raw-data :isgnal :down) rslt)
                       (cons raw-data rslt)))))
               start-list
               (reverse partitioned-join))
       )))

(defn bollinger-band

  ([tick-window tick-list]
     (let [sma-list (analysis/simple-moving-average nil tick-window tick-list)]
       (bollinger-band tick-window tick-list sma-list)))

  ([tick-window tick-list sma-list]


     ;; taken from these videos:
     ;; i. http://www.youtube.com/watch?v=tkwUOUZQZ3s
     ;; ii. http://www.youtube.com/watch?v=7PY4XxQWVfM


     ;; track widest & narrowest band over the last 'n' ( 3 ) ticks
     (let [bband (analysis/bollinger-band tick-window tick-list sma-list)
           diffs (map (fn [inp] (assoc inp :difference (- (:upper-band inp) (:lower-band inp)))) (remove nil? bband))
           sorted-list (sort-by :difference diffs)

           most-narrow (take 3 sorted-list)
           most-wide (take-last 3 sorted-list)]

       )


     ;; A... when the band width is very low, can indicate that price will breakout sooner than later;
     ;; MA is in an UP or DOWN market

     ;; check for narrow bollinger band width
        ;; less than the most previous narrow band width

     ;; close is outside of band, and previous swing high/low is inside the band




     ;; B... when the band width is very high (high volatility); can mean that the trend is ending soon; can i. change direction or ii. consolidate
     ;; MA is in a sideways (choppy) market -> check if many closes that are abouve or below the bollinger band

     ;; check for a wide bollinger band width
        ;; greater than the most previous wide band

     ;; RSI Divergence; i. price makes a higher high and ii. rsi devergence makes a lower high iii. and divergence should happen abouve the overbought line

     ;; entry signal -> check if one of next 3 closes are underneath the priors (or are in the opposite direction)

     ))
