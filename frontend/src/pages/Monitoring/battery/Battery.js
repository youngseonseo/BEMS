import NavigationBar from "../../../components/NavBar/navbar";
import MainHeader from "../../../components/MainHeader/header";
import { BackGround } from "./../energy/EnergyStyle";
import {
  BatteryBackground,
  BatteryNow,
  InsideBattery,
  BatteryOutline,
  CustomBattery,
  SOCGraph1,
  BatteryGraph,
  SOCChange,
} from "./BatteryStyle";
import { useEffect, useState } from "react";
import { EventSourcePolyfill } from "event-source-polyfill";
import axios from "axios";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  LineChart,
  Line,
  Legend,
  ResponsiveContainer,
} from "recharts";

export default function BatteryPage() {
  const [data1, setData1] = useState([]);
  const [data2, setData2] = useState([]);
  const [data3, setData3] = useState([]);

  const [input, setInput] = useState([]);
  const moment = require("moment");
  const token = localStorage.getItem("accessToken");
  const postSSEconnect = () => {
    const subscribeUrl = `http://localhost:8080/api/sub`;

    if (token != null) {
      let eventSourceBattery = new EventSourcePolyfill(subscribeUrl, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/event-stream",
        },
        heartbeatTimeout: 10 * 60 * 1000,
        withCredentials: true,
      });

      eventSourceBattery.onopen = (event) => {
        console.log("this is opened Battery");
      };
      eventSourceBattery.addEventListener("essBatteryScheduling", (event) => {
        const json = JSON.parse(event.data);
        console.log(json);
        setData1((prev) => [...prev, json.graph1]);
        setData2((prev) => [...prev, json.graph2]);
        setData3((prev) => [...prev, json.graph3]);
      });

      eventSourceBattery.addEventListener("error", (e) => {
        console.log("Battery connet Error", e);
        eventSourceBattery.close();
      });
    }
  };
  const formatXAxis = (tickItem) => {
    return `${moment(tickItem).format("M/D hh:mm")}`;
  };
  useEffect(() => {
    postSSEconnect();
    axios.get("http://localhost:8080/api/ess", {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "text/event-stream",
      },
    });
  }, [data1]);
  console.log(data1, data2, data3);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="battery" />
        <BatteryBackground>
          <SOCGraph1>
            <BatteryNow>
              <div style={{ padding: "10px" }}>배터리 잔여량</div>

              <CustomBattery></CustomBattery>
              <BatteryOutline>
                <InsideBattery></InsideBattery>
              </BatteryOutline>

              <div>58%</div>
            </BatteryNow>
            <SOCChange>
              <div>배터리 상태 (State of Charge)</div>
              <AreaChart
                width={800}
                height={230}
                data={data3}
                margin={{
                  top: 10,
                  right: 30,
                  left: 0,
                  bottom: 0,
                }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="timestamp" tickFormatter={formatXAxis} />
                <YAxis />
                <Tooltip />
                <Area
                  type="monotone"
                  dataKey="soc"
                  stroke="#8884d8"
                  fill="#8884d8"
                />
              </AreaChart>
            </SOCChange>
          </SOCGraph1>
          <BatteryGraph>
            배터리 스케줄링
            <ResponsiveContainer width="100%" height="100%">
              <LineChart
                width={500}
                height={300}
                data={data1}
                margin={{
                  top: 5,
                  right: 30,
                  left: 20,
                  bottom: 5,
                }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="timestamp" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="batteryPower" stroke="#8884d8" />
                <Line type="monotone" dataKey="consumption" stroke="red" />
                <Line type="monotone" dataKey="tou" stroke="#82ca9d" />
              </LineChart>
            </ResponsiveContainer>
          </BatteryGraph>
        </BatteryBackground>
      </BackGround>
    </div>
  );
}
