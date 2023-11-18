import NavigationBar from "../../../components/NavBar/navbar";
import MainHeader from "../../../components/MainHeader/header";
import {
  BackGround,
  Graph1,
  GraphContainer,
  EnergyContainer,
  Graph2,
  Graph3,
  SelectBox,
} from "./EnergyStyle";

import { useEffect, useState } from "react";
import { EventSourcePolyfill } from "event-source-polyfill";
import React from "react";
import {
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
} from "recharts";

export default function EnergyConsumptionMonitoring() {
  const token = localStorage.getItem("accessToken");
  const [data1, setData1] = useState([]);
  const [data2, setData2] = useState([]);
  const [data3, setData3] = useState([]);
  const [ghDuration, setGhDuration] = useState("date");

  const postDate = () => {
    const subscribeUrl = `http://localhost:8080/api/monitor/building?duration=${ghDuration}`;

    if (token != null) {
      let eventSource1 = new EventSourcePolyfill(subscribeUrl, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/event-stream",
        },
        heartbeatTimeout: 10000000,
        withCredentials: true,
      });

      eventSource1.onopen = (event) => {
        console.log("this is opened building");
      };
      eventSource1.addEventListener("sendPrevBuildingInfo", (event) => {
        console.log(event.data);
        const json = JSON.parse(event.data);
        setData1(json.graph1);
        setData2(json.graph2);
        setData3(json.graph3);
        eventSource1.close();
      });

      eventSource1.addEventListener("error", (e) => {
        console.log("Building connet Error");
        eventSource1.close();
      });
    }
  };
  const onChangeDuration = (e) => {
    setGhDuration(e.target.value);
  };
  console.log(ghDuration);

  useEffect(() => {
    postDate();
  }, [ghDuration]);

  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="energy_consumption" />
        <EnergyContainer>
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

          <GraphContainer>
            <Graph1>
              <div>전체 전력 소비량/예측량</div>
              <AreaChart
                width={1100}
                height={300}
                data={data1}
                margin={{
                  top: 10,
                  right: 30,
                  left: 0,
                  bottom: 0,
                }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="timestamp" />
                <Label value="date" position="insideBottom" />
                <YAxis />
                <Tooltip />
                <Area
                  name="총 전력 소비량"
                  type="monotone"
                  dataKey="totalConsumption"
                  stroke="#8884d8"
                  fill="#8884d8"
                />
              </AreaChart>
            </Graph1>
            <Graph2>
              <div>동별 전력 소비량</div>
              <BarChart
                width={500}
                height={300}
                data={data2}
                margin={{
                  top: 5,
                  right: 30,
                  left: 20,
                  bottom: 5,
                }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey={ghDuration} />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar name="561동" dataKey="totalAConsumption" fill="#3D87FF" />
                <Bar name="562동" dataKey="totalBConsumption" fill="#A665F5" />
                <Bar name="563동" dataKey="totalCConsumption" fill="#FA995C" />
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
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line
                  name="561동"
                  type="linear"
                  dataKey="totalAConsumption"
                  stroke="#3D87FF"
                  activeDot={{ r: 1 }}
                />
                <Line
                  name="562동"
                  type="linear"
                  dataKey="totalBConsumption"
                  stroke="#A665F5"
                  activeDot={{ r: 1 }}
                />
                <Line
                  name="563동"
                  type="linear"
                  dataKey="totalCConsumption"
                  stroke="#FA995C"
                  activeDot={{ r: 1 }}
                />
              </LineChart>
            </Graph3>
          </GraphContainer>
        </EnergyContainer>
      </BackGround>
    </div>
  );
}
