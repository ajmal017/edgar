(ns ui.graph
  (:use [jayq.core :only [$ css inner]]))



(defn add-signals [initial-list signal-map]

  (reduce (fn [rslt ech]

            (let [default-entry (fn [eF]
                                  {:type "flags"
                                   :data [{:x (-> eF :x)
                                           :title (-> eF :title)
                                           :text (-> eF :text)}]
                                   :color "#5F86B3"
                                   :fillColor "#5F86B3"
                                   :width 16
                                   :style {:color "white"}
                                   :states {:hover { :fillColor "#395C84" }}})]

              ;; SIGNAL Flags
              (case (first ech)

                :moving-average (concat rslt (reduce (fn [rF eF]        ;; second element is a list of signals
                                                       (conj rF (assoc (default-entry eF) :onSeries "ema-list")))
                                                     []
                                                     (second ech)))

                :bollinger-band (concat rslt (reduce (fn [rF eF]
                                                       (conj rF (assoc (default-entry eF) :onSeries "bollinger-list")))
                                                     []
                                                     (second ech)))

                :macd (concat rslt (reduce (fn [rF eF]
                                             (conj rF (assoc (default-entry eF) :onSeries "macd-price-list")))
                                           []
                                           (second ech)))

                :stochastic-oscillator (concat rslt (reduce (fn [rF eF]
                                                              (conj rF (assoc (default-entry eF) :onSeries "k-list")))
                                                            []
                                                            (second ech)))

                :obv (concat rslt (reduce (fn [rF eF]
                                            (conj rF (assoc (default-entry eF) :onSeries "obv-list")))
                                          []
                                          (second ech)))
                "default" rslt)))
          initial-list
          (seq signal-map)))

(defn add-strategies [initial-list strategy-map]

  (reduce (fn [rslt ech]

            (let [default-entry (fn [eF]
                                  {:type "flags"
                                   :data [{:x (-> eF :x)
                                           :title (-> eF :title)
                                           :text (-> eF :text)}]
                                   :color "#5F86B3"
                                   :fillColor "#5F86B3"
                                   :width 16
                                   :style {:color "white"}
                                   :states {:hover { :fillColor "#395C84" }}})]

              (concat rslt (reduce (fn [rF eF]
                                     (conj rF (assoc (default-entry eF) :onSeries "tick-list")))
                                   []
                                   (second ech)))))
          initial-list
          (seq strategy-map)))



(defn build-graph-series-data [dataList signal-map strategy-map]

  (let [initial-list [{:name "Bollinger Band"
                       :id "bollinger-list"
                       :data (reverse (first dataList))
                       :type "arearange"
                       :color "#629DFF"
                       :marker {:enabled true :radius 3}
                       :tooltip {:valueDecimals 2}}
                      {:name "Closing Price"
                       :id "ticklist"
                       :data (reverse (second dataList))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Simple Moving Average"
                       :id "sma-list"
                       :data (reverse (nth dataList 2))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Exponential Moving Average"
                       :id "ema-list"
                       :data (reverse (nth dataList 3))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}


                      ;; MACD Data
                      {:name "MACD Price"
                       :id "macd-price-list"
                       :data (reverse (nth dataList 4))
                       :yAxis 1
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "MACD Signal"
                       :id "macd-signal-list"
                       :data (reverse (nth dataList 5))
                       :yAxis 1
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      {:name "MACD Histogram"
                       :id "macd-histogram-list"
                       :data (reverse (nth dataList 6))
                       :yAxis 2
                       :type "column"
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      ;; Stochastic Data
                      {:name "Stochastic K"
                       :id "k-list"
                       :data (reverse (nth dataList 7))
                       :yAxis 3
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Stochastic D"
                       :id "d-list"
                       :data (reverse (nth dataList 8))
                       :yAxis 3
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      {:name "On Balance Volume"
                       :id "obv-list"
                       :data (reverse (nth dataList 9))
                       :yAxis 4
                       :type "column"
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:type "flags"
                       :name "strategies"
                       :data []
                       :onSeries "ticklist"
                       :shape "squarepin"
                       }
                      #_{:type "flags"
                       :name "strategies"
                       :shape "squarepin"
                       :data []
                       :onSeries "tick-list"
                       :color "#5F86B3"
                       :fillColor "#5F86B3"
                       :width 16
                       :style {:color "white"}
                       :states {:hover { :fillColor "#395C84" }}}
                      ]

        #_with-signals #_(add-signals initial-list signal-map)  ;; iterate over map entries
        #_with-strategies #_(add-strategies initial-list strategy-map)
        ]

    #_(.log js/console (str "... FINAL series array[" with-strategies "]"))
    initial-list))


(defn chart-fill [selector dataList signal-map strategy-map]

  (-> ($ selector)
      (.highcharts "StockChart" (clj->js
                                 {:names [label "Bolling Band" "Simple Moving Average" "Exponential Moving Average"]
                                  :rangeSelector {:selected 11}
                                  :title {:text label}
                                  :chart {:zoomType "x"}
                                  :navigator {:adaptToUpdatedData true}
                                  :yAxis [{
                                           :title {:text "Technical Analysis"}
                                           :height 200}
                                          {
                                           :title {:text "MACD / Signal"}
                                           :height 100
                                           :top 300
                                           :offset 0
                                           :lineWidth 2}
                                          {
                                           :title {:text "MACD Histog"}
                                           :height 100
                                           :top 400
                                           :offset 0
                                           :lineWidth 2}
                                          {
                                           :title {:text "Stochastic Osc"}
                                           :height 100
                                           :top 500
                                           :offset 0
                                           :lineWidth 2
                                           :max 1
                                           :min 0
                                           :plotLines [{
                                                        :value 0.75
                                                        :color "red"
                                                        :width 1
                                                        :dashStyle "longdash"
                                                        :label {:text "Overbought"}}
                                                       {
                                                        :value 0.25
                                                        :color "green"
                                                        :width 1
                                                        :dashStyle "longdash"
                                                        :label {:text "Oversold"}}]}
                                          {
                                           :title {:text "OBV"}
                                           :height 100
                                           :top 600
                                           :offset 0
                                           :lineWidth 2}]

                                  :series (build-graph-series-data dataList signal-map strategy-map)}))))


(defn chart-increment [selector dataList strategy-map]

  (do

      (-> ($ selector)
          (.highcharts)
          (.-series)
          first
          (.addPoint (last (reverse (first dataList))) true false))

      (-> ($ selector)
          (.highcharts)
          (.-series)
          second
          (.addPoint (last (reverse (second dataList))) true false))

      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 2)
          (.addPoint (last (reverse (nth dataList 2))) true false))

      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 3)
          (.addPoint (last (reverse (nth dataList 3))) true false))


      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 4)
          (.addPoint (last (reverse (nth dataList 4))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 5)
          (.addPoint (last (reverse (nth dataList 5))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 6)
          (.addPoint (last (reverse (nth dataList 6))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 7)
          (.addPoint (last (reverse (nth dataList 7))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 8)
          (.addPoint (last (reverse (nth dataList 8))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 9)
          (.addPoint (last (reverse (nth dataList 9))) true false))


      (.log js/console "")
      (.log js/console (str "Zzz 1[" (last (reverse (second dataList))) "]"))
      (.log js/console (str "Zzz 2[" {:x (js/window.Date. (first (last (reverse (second dataList))))) :title "Testing 123"} "]"))


      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 10)
          (.addPoint {:x (js/window.Date. (first (last (reverse (second dataList))))) :title "Testing 123"} true false))

      #_(reduce (fn [rslt ech]

                (let [default-entry (fn [eF]
                                      {:x (-> eF :x)
                                       :title (-> eF :title)
                                       :text (-> eF :text)})]

                  (concat rslt (reduce (fn [rF eF]

                                         #_(.log js/console (str "... AND > eF[" eF "]"))
                                         (let [strategy-entry (default-entry eF)]

                                           #_(.log js/console (str "... AND AND > strategy-entry[" strategy-entry "] > fn["  "]"))
                                           (-> ($ selector)
                                               (.highcharts)
                                               (.-series)
                                               (nth 10)
                                               (.addPoint strategy-entry true false))
                                           (conj rF strategy-entry)))
                                       []
                                       (second ech)))))
              []
              (seq strategy-map))))

(defn render-stock-graph [selector dataList signal-map strategy-map label increment]


  #_(.log js/console (str "... render-stock-graph > strategy-map[" strategy-map "]"))
  (if-not increment

    (chart-fill selector dataList signal-map strategy-map)
    (chart-increment selector dataList strategy-map)))