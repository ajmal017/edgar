(ns edgar.scratch)


;; Ad-Hoc connect get a day's worth of IBM historical data
(def data-list (ref []))
(defn handler-fn [evt] (dosync (alter data-list conj evt)))

(require '[edgar.ib.market :as market])
(market/subscribe-to-market handler-fn)

(def connect-result (market/connect-to-market))
; user> connect-result
; {:esocket #<EClientSocket com.ib.client.EClientSocket@7cd84a10>, :ewrapper #<EWrapperImpl com.interrupt.edgar.EWrapperImpl@55877de5>}

(market/request-historical-data (:esocket connect-result) 0 "IBM" "1 D" "1 secs" "TRADES")

;; re: [a-z0-9]+-[a-z0-9]+-[a-z0-9]+-[a-z0-9]+-[a-z0-9]+

(def tick-list [{:uuid "a34661f5-c115-43a3-ae02-6ae88e02198c" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84576453 :total-volume 25907 :last-trade-time 1368215573010 :last-trade-size 1 :last-trade-price 203.98} {:uuid "681f16a0-647d-427a-ab11-6ba6fce217d1" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.8457701 :total-volume 25908 :last-trade-time 1368215576331 :last-trade-size 1 :last-trade-price 203.99} {:uuid "a4bb6071-cfc9-4ecc-b72e-d336f9cbc61c" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84577567 :total-volume 25909 :last-trade-time 1368215576857 :last-trade-size 1 :last-trade-price 203.99} {:uuid "78141a2b-1caa-4939-b182-d29cc59395d0" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.8457868 :total-volume 25911 :last-trade-time 1368215577765 :last-trade-size 2 :last-trade-price 203.99} {:uuid "a4b2b3c1-2d70-486a-881a-7f029b9be13f" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84579275 :total-volume 25912 :last-trade-time 1368215578769 :last-trade-size 1 :last-trade-price 204.00} {:uuid "bc61e5eb-3dce-42ac-81de-2918fc6bb1f3" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84579909 :total-volume 25913 :last-trade-time 1368215579272 :last-trade-size 1 :last-trade-price 204.01} {:uuid "b4fbfc85-8ba9-4e75-84b6-d48344b5bcb5" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84580581 :total-volume 25914 :last-trade-time 1368215579517 :last-trade-size 1 :last-trade-price 204.02} {:uuid "34582cc3-f5a2-44c3-8e92-1828b6bc64cb" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84581253 :total-volume 25915 :last-trade-time 1368215581769 :last-trade-size 1 :last-trade-price 204.02} {:uuid "33ffc093-ae85-4a59-8bff-8666c5db600f" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84581906 :total-volume 25916 :last-trade-time 1368215583602 :last-trade-size 1 :last-trade-price 204.01} {:uuid "7fd9bf4e-b111-496a-9019-af6f22106d86" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84582578 :total-volume 25917 :last-trade-time 1368215585650 :last-trade-size 1 :last-trade-price 204.02} {:uuid "340f0818-27b5-497b-a159-2624a7f78b6a" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.8458325 :total-volume 25918 :last-trade-time 1368215586060 :last-trade-size 1 :last-trade-price 204.02} {:uuid "9e761a6f-644d-42bc-9b81-05b985da9860" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84583883 :total-volume 25919 :last-trade-time 1368215587029 :last-trade-size 1 :last-trade-price 204.01} {:uuid "d4b56c12-8925-4539-ad05-a8f7197f5e93" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84584555 :total-volume 25920 :last-trade-time 1368215588318 :last-trade-size 1 :last-trade-price 204.02} {:uuid "e8c4a1fa-f857-484a-b2d1-7989e1390e6e" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.8458962 :total-volume 25928 :last-trade-time 1368215589335 :last-trade-size 8 :last-trade-price 204.01} {:uuid "e8fab1a8-c744-427e-a80c-e1caff24e578" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84590253 :total-volume 25929 :last-trade-time 1368215589536 :last-trade-size 1 :last-trade-price 204.01} {:uuid "f46948a2-d8eb-41ea-8c53-8887485a5c7e" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84597612 :total-volume 25941 :last-trade-time 1368215589846 :last-trade-size 12 :last-trade-price 204.00} {:uuid "47e9da78-8820-4c4e-8225-fceed176b8e8" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84598168 :total-volume 25942 :last-trade-time 1368215591079 :last-trade-size 1 :last-trade-price 203.99} {:uuid "5142c443-34b8-464c-9260-a804fdc32490" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84599245 :total-volume 25944 :last-trade-time 1368215591789 :last-trade-size 2 :last-trade-price 203.99} {:uuid "18db4328-312d-41f7-90ac-b7393d1ff8fc" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84599761 :total-volume 25945 :last-trade-time 1368215592104 :last-trade-size 1 :last-trade-price 203.98} {:uuid "1a1e1d01-7279-411b-8ea3-fe20068fc684" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.8460131 :total-volume 25948 :last-trade-time 1368215592615 :last-trade-size 3 :last-trade-price 203.98} {:uuid "4b9b31ba-52b0-4431-8743-4518c85c6327" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84602382 :total-volume 25950 :last-trade-time 1368215592758 :last-trade-size 2 :last-trade-price 203.99} {:uuid "0d2f2779-40b6-4ba6-85b4-67175ecd365e" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84603815 :total-volume 25953 :last-trade-time 1368215594039 :last-trade-size 3 :last-trade-price 203.97} {:uuid "71ac0fda-6a96-4589-8a60-d1091aef40b1" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84604331 :total-volume 25954 :last-trade-time 1368215597119 :last-trade-size 1 :last-trade-price 203.98} {:uuid "82e94d05-a449-40fa-ae58-d05bc994fb78" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84605763 :total-volume 25957 :last-trade-time 1368215597632 :last-trade-size 3 :last-trade-price 203.97} {:uuid "58b0f3b5-dbbe-41c1-947d-a54e2056ea6a" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84606241 :total-volume 25958 :last-trade-time 1368215599396 :last-trade-size 1 :last-trade-price 203.97} {:uuid "976fb5c9-4d07-4392-b5f2-97e11d08b229" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.8460668 :total-volume 25959 :last-trade-time 1368215603876 :last-trade-size 1 :last-trade-price 203.96} {:uuid "fac88ea5-86f2-4a50-af39-32bb492eedc7" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84607558 :total-volume 25961 :last-trade-time 1368215606059 :last-trade-size 2 :last-trade-price 203.96} {:uuid "cc0f2c58-3a86-49d6-beba-5d9c7b376229" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84611405 :total-volume 25971 :last-trade-time 1368215610316 :last-trade-size 10 :last-trade-price 203.95} {:uuid "0ae67ad8-0a78-4bda-90a6-1231fbb8109b" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84611786 :total-volume 25972 :last-trade-time 1368215610634 :last-trade-size 1 :last-trade-price 203.95} {:uuid "a1dcd570-fbd1-4e5e-a449-f9cd9f2371cb" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84612755 :total-volume 25975 :last-trade-time 1368215610813 :last-trade-size 3 :last-trade-price 203.93} {:uuid "b204ded1-bffd-4b44-9e4b-55a1531ec7c0" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84613155 :total-volume 25976 :last-trade-time 1368215612886 :last-trade-size 1 :last-trade-price 203.95} {:uuid "cb1ffde4-87d9-4308-983c-6b0ae7bbc77b" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84614961 :total-volume 25981 :last-trade-time 1368215615858 :last-trade-size 5 :last-trade-price 203.94} {:uuid "db8c463d-39ff-453f-ba02-b2146786a218" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84615322 :total-volume 25982 :last-trade-time 1368215618621 :last-trade-size 1 :last-trade-price 203.94} {:uuid "fabecb9a-b023-4c57-af42-191266678350" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.8461576 :total-volume 25983 :last-trade-time 1368215619138 :last-trade-size 1 :last-trade-price 203.96} {:uuid "57e332f9-45af-470c-886b-f5e5ea598d06" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84616483 :total-volume 25985 :last-trade-time 1368215623846 :last-trade-size 2 :last-trade-price 203.94} {:uuid "e9bcdfd5-f5ac-4a92-8523-b371c29ba43a" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84616844 :total-volume 25986 :last-trade-time 1368215632669 :last-trade-size 1 :last-trade-price 203.94} {:uuid "71cbea87-59fe-4be2-b74d-8afed0988c23" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84618057 :total-volume 25990 :last-trade-time 1368215634709 :last-trade-size 4 :last-trade-price 203.92} {:uuid "7c784665-cd70-448c-882b-5813d3a1aa2a" :type "tickString" :tickerId 0 :single-trade-flag false :vwap 203.84618721 :total-volume 25992 :last-trade-time 1368215636587 :last-trade-size 2 :last-trade-price 203.93} {:uuid "c7a22351-24dc-46f9-a602-a910aaf4ec3e" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84619082 :total-volume 25993 :last-trade-time 1368215636952 :last-trade-size 1 :last-trade-price 203.94} {:uuid "95ae6b4c-7cd6-42eb-8abd-0a0a8c0a33df" :type "tickString" :tickerId 0 :single-trade-flag true :vwap 203.84619405 :total-volume 25994 :last-trade-time 1368215638328 :last-trade-size 1 :last-trade-price 203.93}])

(def to-client (map (fn [inp] [ (:last-trade-time inp) (:last-trade-price inp)]) tick-list))

;; ([1368215573010 203.98] [1368215576331 203.99] [1368215576857 203.99] [1368215577765 203.99] [1368215578769 204.0] [1368215579272 204.01] [1368215579517 204.02] [1368215581769 204.02] [1368215583602 204.01] [1368215585650 204.02] [1368215586060 204.02] [1368215587029 204.01] [1368215588318 204.02] [1368215589335 204.01] [1368215589536 204.01] [1368215589846 204.0] [1368215591079 203.99] [1368215591789 203.99] [1368215592104 203.98] [1368215592615 203.98] [1368215592758 203.99] [1368215594039 203.97] [1368215597119 203.98] [1368215597632 203.97] [1368215599396 203.97] [1368215603876 203.96] [1368215606059 203.96] [1368215610316 203.95] [1368215610634 203.95] [1368215610813 203.93] [1368215612886 203.95] [1368215615858 203.94] [1368215618621 203.94] [1368215619138 203.96] [1368215623846 203.94] [1368215632669 203.94] [1368215634709 203.92] [1368215636587 203.93] [1368215636952 203.94] [1368215638328 203.93])

;; TEST Setup for live play

(use '[clojure.repl])
(require '[edgar.tee.play :as tplay])
(require '[edgar.ib.handler.live :as live])

(def tick-list (ref (read-string (slurp "etc/test-live-list.edn"))))
(def tee-list [(partial tplay/tee-market @tick-list)])

(def run-me (partial live/feed-handler {:tick-list tick-list :ticker-id-filter [1] :tee-list tee-list}))

#_(run-me (read-string (slurp "etc/test-live-latest-event.edn")))


;; =====
(def t1 (sleading/macd-signal-crossover macd-list))
(def t2 (sleading/macd-divergence 10 macd-list))

(def t3 (sleading/stochastic-level k-list))
(def t4 (sleading/stochastic-crossover (partition 2 1 k-list)))
(def t5 (sleading/stochastic-divergence 10 k-list))
(def t6 (sleading/stochastic-oscillator 14 3 3 live-list k-list))


;; TODO - Stategies, where we compose filters and signals
   ;; -- consider signals against trends
   ;; -- put stop-losses against support / resistance
   ;; -- find good market, where price movement is i big and ii steady
