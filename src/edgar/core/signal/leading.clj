(ns edgar.core.signal.leading
  (:require [edgar.core.analysis.leading :as lead-analysis]
            [edgar.core.analysis.lagging :as lag-analysis]))


(defn macd
  "Functions searches for signals to overlay on top of a regular MACD time series. It uses the following strategies

   A. MACD / signal crossover
      when i. MACD line crosses over the ii. signal line

   B. MACD divergence
      when i. closing price makes a higher high and ii. MACD makes a lower high
      when price rises and falls quickly

      OR

      look for high price resistance over last 3 peaks
      when i. closing price makes a higher high and ii. histogram makes a lower high

      after both are true, look for

         i. subsequent 3 closing prices to be below the high

         OR

         ii. if histogram goes into negative territory

   ... TODO - C. MACD Stairsteps (http://www.youtube.com/watch?v=L-cB_zZcpks)

      ENTRY:
         when i. MACD crosses over ii. the signal line
         when subsequent 3 low(s) are equal or greater than the previous high(s)

      EXIT:
         measure last up-move and project target (difference from last high, from low); stop below the current low
  "

  ([options tick-window tick-list]
     (macd options tick-window tick-list (lag-analysis/simple-moving-average nil tick-window tick-list)))

  ([options tick-window tick-list sma-list]
     (let [macd-list (lead-analysis/macd options tick-window tick-list sma-list)]
       (macd options tick-window tick-list sma-list macd-list)))

  ([options tick-window tick-list sma-list macd-list]


     ;; A.


     ;; B.


     ;; C.


     )
)
