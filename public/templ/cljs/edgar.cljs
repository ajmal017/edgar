(ns edgar
  (:use [jayq.core :only [$ css inner]])
  (:use-macros [jayq.macros :only [let-ajax let-deferred]])
  (:require [jayq.core :as jq]
            [cljs.reader :as reader]))



;; === SCROLLING  with Lionbars
#_(.lionbars ($ ".body-container"))


(defn build-graph-series-data [dataList signal-map]

  (let [initial-list [{:name "Bollinger Band"
                       :data (reverse (first dataList))
                       :type "arearange"
                       :color "#629DFF"
                       :marker {:enabled true :radius 3}
                       :tooltip {:valueDecimals 2}}
                      {:name label
                       :id "tick-list"
                       :data (reverse (second dataList))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Simple Moving Average"
                       :data (reverse (nth dataList 2))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Exponential Moving Average"
                       :data (reverse (nth dataList 3))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}


                      ;; MACD Data
                      {:name "MACD Price"
                       :data (reverse (nth dataList 4))
                       :yAxis 1
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "MACD Signal"
                       :data (reverse (nth dataList 5))
                       :yAxis 1
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      {:name "MACD Histogram"
                       :data (reverse (nth dataList 6))
                       :yAxis 2
                       :type "column"
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      ;; Stochastic Data
                      {:name "Stochastic K"
                       :data (reverse (nth dataList 7))
                       :yAxis 3
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Stochastic D"
                       :data (reverse (nth dataList 8))
                       :yAxis 3
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      {:name "On Balance Volume"
                       :data (reverse (nth dataList 9))
                       :yAxis 4
                       :type "column"
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      ]

        with-signals (reduce (fn [rslt ech]

                               ;; SIGNAL Flags
                               (if (= :moving-average (first ech))

                                 (conj rslt {:type "flags"
                                             :data [{:x (-> ech second :x)
                                                     :title (-> ech second :title)
                                                     :text (-> ech second :title)}
                                                    ]
                                             :color "#5F86B3"
                                             :fillColor "#5F86B3"
                                             :onSeries "tick-list"
                                             :width 16
                                             :style {:color "white"}
                                             :states {:hover { :fillColor "#395C84" }}})))
                             initial-list
                             (seq signal-map))  ;; iterate over map entries

        ]))

;; === RENDER the Live stock graph
(defn render-stock-graph [selector dataList signal-map label increment]

  (if-not increment

    (-> ($ selector)
        (.highcharts "StockChart" (clj->js
                                   {:names [label "Bolling Band" "Simple Moving Average" "Exponential Moving Average"]
                                    :rangeSelector {:selected 7}
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

                                    :series (build-graph-series-data)})))
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

      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 2)
          (.addPoint (last (reverse (nth dataList 2))) true false))

      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 3)
          (.addPoint (last (reverse (nth dataList 3))) true false))


      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 4)
          (.addPoint (last (reverse (nth dataList 4))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 5)
          (.addPoint (last (reverse (nth dataList 5))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 6)
          (.addPoint (last (reverse (nth dataList 6))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 7)
          (.addPoint (last (reverse (nth dataList 7))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 8)
          (.addPoint (last (reverse (nth dataList 8))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 9)
          (.addPoint (last (reverse (nth dataList 9))) true false)))))



;; === POPULATE the live multi-select
(defn populate-multiselect [selector options]

  (let-deferred [filtered-input ($/ajax "/list-filtered-input")]

                (let [multiselect ($ selector)]

                  (reduce (fn [rslt inp]

                            (let [option-value (second inp)
                                  option-label (nth inp 2)
                                  price-difference (.toFixed (first inp) 2)]

                              (-> multiselect
                                  (.append (str "<option value='" option-value "'>" option-label " (" price-difference ")</option>")))))
                          nil
                          (into-array (reader/read-string filtered-input)))

                  (-> ($ selector)
                      (.multiselect (clj->js (merge {:enableFiltering true} options)))))))



(defn parse-result-data [result-data]

  {:local-list (into-array (reduce (fn [rslt ech]
                                     (conj rslt (into-array [(js/window.parseInt (first ech))
                                                             (js/window.parseFloat (second ech))])))
                                   []
                                   (into-array (:stock-list result-data))))


   ;; Basic Long and Short Moving Averages
   :sma-list (into-array (reduce (fn [rslt ech]
                                   (conj rslt (into-array [(js/window.parseInt (first ech))
                                                           (js/window.parseFloat (second ech))])))
                                 []
                                 (remove #(nil? (first %))
                                         (into-array (:sma-list result-data)))))

   :ema-list (into-array (reduce (fn [rslt ech]
                                   (conj rslt (into-array [(js/window.parseInt (first ech))
                                                           (js/window.parseFloat (second ech))])))
                                 []
                                 (remove #(nil? (first %))
                                         (into-array (:ema-list result-data)))))


   ;; Bollinger-Band Data
   :bollinger-band (into-array (reduce (fn [rslt ech]
                                         (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                 (js/window.parseFloat (:lower-band ech))
                                                                 (js/window.parseFloat (:upper-band ech))])))
                                       []
                                       (remove nil? (-> result-data :signals :bollinger-band))))

   ;; MACD Data
   :macd-price-list (into-array (reduce (fn [rslt ech]
                                         (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                 (js/window.parseFloat (:last-trade-macd ech))])))
                                       []
                                       (remove nil? (-> result-data :signals :macd))))

   :macd-signal-list (into-array (reduce (fn [rslt ech]
                                           (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                   (js/window.parseFloat (:ema-signal ech))])))
                                         []
                                         (remove nil? (-> result-data :signals :macd))))

   :macd-histogram-list (into-array (reduce (fn [rslt ech]
                                              (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                      (js/window.parseFloat (:histogram ech))])))
                                            []
                                            (remove nil? (-> result-data :signals :macd))))

   ;; Stochastic Oscillator
   :stochastic-k (into-array (reduce (fn [rslt ech]
                                       (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                               (js/window.parseFloat (:K ech))])))
                                     []
                                     (remove nil? (-> result-data :signals :stochastic-oscillator))))

   :stochastic-d (into-array (reduce (fn [rslt ech]
                                       (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                               (js/window.parseFloat (:D ech))])))
                                     []
                                     (remove nil? (-> result-data :signals :stochastic-oscillator))))

   :obv (into-array (reduce (fn [rslt ech]
                              (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                      (js/window.parseInt (:obv ech))])))
                            []
                            (remove nil? (-> result-data :signals :obv))))

   :signals {:moving-average (remove empty? (into-array (reduce (fn [rslt ech]
                                                                   (conj rslt (map (fn [inp]   ;; iterate over the :signals list, for each tick entry
                                                                                     {:x (js/window.parseInt (:last-trade-time ech))
                                                                                      :title (:signal inp)
                                                                                      :text (str "Why: " (:why inp))
                                                                                      })
                                                                                   (:signals ech))))
                                                                 []
                                                                 (remove nil? (-> result-data :signals :moving-average)))))}

   :stock-name (:stock-name result-data)})


(populate-multiselect ".multiselect-live" {:onChange (fn [element checked]

                                                       (if checked
                                                         ($/post (str "/get-streaming-stock-data?stock-selection=" (.val element) "&stock-name=" (.text element))
                                                                 (fn [data]
                                                                   (.log js/console (str "POST:: get-streaming-stock-data > data[" data "]"))))))})
(.click ($ "#freeform-live") (fn [eventObj]

                               (let [input-val (.val ($ "#freeform-live-input"))]

                                 (.log js/console "... here[" eventObj "] / input[" input-val "]")
                                 (if (not (empty? input-val))

                                   ($/post (str "/get-streaming-stock-data?stock-selection=" input-val "&stock-name=" input-val)
                                           (fn [data]
                                             (.log js/console (str "POST:: get-streaming-stock-data > data[" data "]"))))))))


(populate-multiselect ".multiselect-historical" {:onChange (fn [element checked]

                                                             (if checked
                                                               ($/ajax "/get-historical-data"
                                                                       (clj->js {:data {:stock-selection (.val element)
                                                                                        :time-duration "300 S"
                                                                                        :time-interval "1 secs"}
                                                                                 :complete (fn [jqXHR status]

                                                                                             (.log js/console (str ".multiselect-historical > jqXHR[" jqXHR "] / status[" status "]"))
                                                                                             (let [result-data (reader/read-string (.-responseText jqXHR))
                                                                                                   parsed-result-map (parse-result-data result-data)
                                                                                                   increment? false]

                                                                                               (.log js/console (str ".multiselect-historical > signals[" (:signals parsed-result-map) "]"))
                                                                                               (render-stock-graph "#historical-stock-graph"
                                                                                                                   (:signals parsed-result-map)
                                                                                                                   [(:bollinger-band parsed-result-map)
                                                                                                                    (:local-list parsed-result-map)
                                                                                                                    (:sma-list parsed-result-map)
                                                                                                                    (:ema-list parsed-result-map)


                                                                                                                    (:macd-price-list parsed-result-map)
                                                                                                                    (:macd-signal-list parsed-result-map)
                                                                                                                    (:macd-histogram-list parsed-result-map)

                                                                                                                    (:stochastic-k parsed-result-map)
                                                                                                                    (:stochastic-d parsed-result-map)

                                                                                                                    (:obv parsed-result-map)]
                                                                                                                   (:stock-name parsed-result-map)
                                                                                                                   increment?)))}))))})

(def livesource (js/window.EventSource. "/get-streaming-stock-data"))
(.addEventListener livesource
                   "stream-live"
                   (fn [e]

                     (let [result-data (reader/read-string (.-data e))
                           parsed-result-map (parse-result-data result-data)
                           increment?  (and (not (nil? (-> ($ "#live-stock-graph") (.highcharts "StockChart"))))
                                            (= (:stock-name parsed-result-map)
                                               (-> ($ "#live-stock-graph") (.highcharts "StockChart") (.-title) (.-text)))) ]

                       (render-stock-graph "#live-stock-graph"
                                           (:signals parsed-result-map)
                                           [(:bollinger-band parsed-result-map)
                                            (:local-list parsed-result-map)
                                            (:sma-list parsed-result-map)
                                            (:ema-list parsed-result-map)


                                            (:macd-price-list parsed-result-map)
                                            (:macd-signal-list parsed-result-map)
                                            (:macd-histogram-list parsed-result-map)

                                            (:stochastic-k parsed-result-map)
                                            (:stochastic-d parsed-result-map)

                                            (:obv parsed-result-map)]
                                           (:stock-name parsed-result-map)
                                           increment?))))
