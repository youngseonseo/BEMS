import NavigationBar from "../../../components/NavBar/navbar";
import MainHeader from "../../../components/MainHeader/header";
import {
  BackGround,
  Graph1,
  GraphContainer,
  Graph2Container,
  EnergyContainer,
  DurationSelect,
  Graph2,
  Graph3,
  SelectBox,
} from "./EnergyStyle";

import { useEffect, useState } from "react";
import { EventSourcePolyfill } from "event-source-polyfill";
import React from "react";
import {
  ComposedChart,
  BarChart,
  Bar,
  AreaChart,
  Area,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  Label,
  Cell,
} from "recharts";

export default function EnergyConsumptionMonitoring() {
  const token = localStorage.getItem("accessToken");
  const [data1, setData1] = useState([]);
  const [data2, setData2] = useState([]);
  const [data3, setData3] = useState([]);
  const [input, setInput] = useState([]);
  const [Check, setCheck] = useState(false);
  const [ghDuration, setGhDuration] = useState("date");
  const moment = require("moment");

  const postDate = () => {
    const subscribeUrl = `http://localhost:8080/api/monitor/building?duration=${ghDuration}`;

    if (token != null) {
      let eventSource1 = new EventSourcePolyfill(subscribeUrl, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/event-stream",
        },
        heartbeatTimeout: 10 * 60 * 1000,
        withCredentials: true,
      });

      eventSource1.onopen = (event) => {
        console.log("this is opened building");
      };
      eventSource1.addEventListener("sendPrevBuildingInfo", (event) => {
        const json = JSON.parse(event.data);
        setData1(json.graph1);
        setData2(json.graph2);
        setData3(json.graph3);
      });

      eventSource1.addEventListener("graph1", (event) => {
        const json = JSON.parse(event.data);
        console.log(json);
        setInput(json);
        setData1((prev) => [...prev, json]);
      });

      eventSource1.addEventListener("error", (e) => {
        console.log("Building connet Error");
        setCheck(true);
        eventSource1.close();
      });
    }
  };
  console.log(data1);
  const onChangeDuration = (e) => {
    setGhDuration(e.target.value);
  };
  const formatXAxisGraph1 = (tickItem) => {
    return `${moment(tickItem).format("M/D")}`;
  };
  const formatXAxis = (tickItem) => {
    if (tickItem && ghDuration === "date")
      return `${moment(tickItem).format("M/D")}`;
    else if (tickItem && ghDuration === "week")
      return `${moment(tickItem).format("M월D주")}`;
    else if (tickItem && ghDuration === "month")
      return `${moment(tickItem).format("M")}월`;
    else return tickItem;
  };
  const formatYAxis = (tickItem) => tickItem.toLocaleString();
  const data2format = [
    { name: "561동", value: data2.totalAConsumption },
    { name: "562동", value: data2.totalBConsumption },
    { name: "563동", value: data2.totalCConsumption },
  ];
  const colors = ["#3D87FF", "#A665F5", "#FA995C"];

  useEffect(() => {
    postDate();
  }, [ghDuration]);

  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="energy_consumption" />
        <EnergyContainer>
          <GraphContainer>
            <Graph1>
              <div>전체 전력 소비량/예측량</div>
              <ComposedChart
                width={1100}
                height={300}
                data={data1}
                margin={{
                  top: 40,
                  right: 40,
                  left: 20,
                  bottom: 0,
                }}
              >
                <CartesianGrid
                  strokeDasharray={3}
                  vertical={false}
                  orientation={0}
                />
                <XAxis
                  dataKey="timestamp"
                  tickFormatter={formatXAxisGraph1}
                  interval={24}
                />
                <Label value="date" position="insideBottom" />
                <Legend />
                <YAxis
                  label={{
                    value: "kW",
                    offset: 30,
                    position: "top",
                  }}
                />
                <Tooltip />
                <Area
                  name="총 전력 소비량"
                  type="monotone"
                  dataKey="totalConsumption"
                  stroke="#8884d8"
                  fill="#8884d8"
                />

                <Line
                  name="총 전력 예측량"
                  type="monotone"
                  dataKey="totalPredictConsumption"
                  stroke="red"
                  fill="gray"
                />
              </ComposedChart>
            </Graph1>
            <DurationSelect>
              기간 설정
              <SelectBox onChange={onChangeDuration}>
                <option key={"date"} value={"date"}>
                  date
                </option>
                <option key={"week"} value={"week"}>
                  week
                </option>
                <option key={"month"} value={"month"}>
                  month
                </option>
              </SelectBox>
            </DurationSelect>

            <Graph2Container>
              <Graph2>
                <div>동별 전력 소비량</div>
                <BarChart
                  width={500}
                  height={300}
                  data={data2format}
                  margin={{
                    top: 5,
                    right: 20,
                    left: 30,
                    bottom: 5,
                  }}
                >
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis tickFormatter={formatYAxis} />
                  <Tooltip />
                  <Bar dataKey="value" barSize={50}>
                    {data2format.map((value, index) => (
                      <Cell
                        key={`cell-${index}`}
                        fill={colors[index]}
                        strokeWidth={3}
                      />
                    ))}
                  </Bar>
                </BarChart>
              </Graph2>
              <Graph3>
                <div>동별 전력 소비 비교</div>
                <LineChart
                  width={500}
                  height={300}
                  data={data3}
                  margin={{
                    top: 5,
                    right: 20,
                    left: 20,
                    bottom: 5,
                  }}
                >
                  <CartesianGrid strokeDasharray="2 2" />
                  <XAxis
                    dataKey="date"
                    tickFormatter={formatXAxis}
                    interval={0}
                  />
                  <YAxis
                    tickFormatter={formatYAxis}
                    label={{
                      value: "kW",
                      offset: 30,
                      position: "top",
                    }}
                  />
                  <Tooltip />
                  <Legend />
                  <Line
                    name="561동"
                    type="linear"
                    dataKey="totalAConsumption"
                    stroke="#3D87FF"
                  />
                  <Line
                    name="562동"
                    type="linear"
                    dataKey="totalBConsumption"
                    stroke="#A665F5"
                  />
                  <Line
                    name="563동"
                    type="linear"
                    dataKey="totalCConsumption"
                    stroke="#FA995C"
                  />
                </LineChart>
              </Graph3>
            </Graph2Container>
          </GraphContainer>
        </EnergyContainer>
      </BackGround>
    </div>
  );
}
