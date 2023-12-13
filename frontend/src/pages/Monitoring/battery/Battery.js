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
import { useEffect } from "react";

import {
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  LineChart,
  Line,
  Legend,
  ResponsiveContainer,
} from "recharts";

export default function BatteryPage({ data1, data2, data3 }) {
  // const [data1, setData1] = useState([]);
  // const [data2, setData2] = useState([]);
  // const [data3, setData3] = useState([]);
  // const prevdata = [];
  const moment = require("moment");

  // const ESSScheduling = () => {
  //   const token = localStorage.getItem("accessToken");
  //   const subscribeUrl = "http://localhost:8080/api/ess";

  //   if (token != null) {
  //     console.log("SSE ", token);
  //     let eventSourceess = new EventSourcePolyfill(subscribeUrl, {
  //       headers: {
  //         Authorization: `Bearer ${token}`,
  //         "Content-Type": "text/event-stream",
  //       },
  //       heartbeatTimeout: 10 * 60 * 1000,
  //       withCredentials: true,
  //     });

  //     eventSourceess.onopen = (event) => {
  //       console.log("this is opened", event);

  //       //if (sendBattery === false && token != null) {
  //       //   SendingStart();
  //       // }
  //     };
  //     eventSourceess.addEventListener("essBatteryScheduling", (event) => {
  //       const json = JSON.parse(event.data);
  //       setSendBattery(true);
  //       setData1((prev) => [...prev, json.graph1]);
  //       setData2((prev) => [...prev, json.graph2]);
  //       setData3((prev) => [...prev, json.graph3]);
  //     });
  //     eventSourceess.addEventListener("getEssSchPrevData", (event) => {
  //       const json = JSON.parse(event.data);
  //       console.log(json);
  //       setData1(json.graph1);
  //       setData2(json.graph2);
  //       setData3(json.graph3);
  //     });

  //     eventSourceess.addEventListener("error", (e) => {
  //       console.log("An error occurred while attempting to connect.");
  //       eventSourceess.close();
  //     });
  //   }
  // };

  const formatXAxis = (tickItem) => {
    return `${moment(tickItem).format("M/D(hh:mm)")}`;
  };
  const formatYAxis = (tickItem) => tickItem.toFixed(2);

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip">
          <p className="label">{`${formatXAxis(label)}`}</p>
          <p className="label">{`배터리 잔여량 : ${payload[0]?.value.toFixed(
            2
          )}%`}</p>
        </div>
      );
    }

    return null;
  };

  const CustomTooltip2 = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip">
          <p className="label">{`${formatXAxis(label)}`}</p>
          <p className="label">{`${
            payload[0].name
          } : ${payload[0]?.value.toFixed(2)}`}</p>
          <p className="label">{`${
            payload[1].name
          } : ${payload[1]?.value.toFixed(2)}kW`}</p>
          <p className="label">{`${
            payload[2].name
          } : ${payload[2]?.value.toFixed(2)}(원/kwh)`}</p>
        </div>
      );
    }

    return null;
  };
  const CustomTooltip3 = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip">
          <p className="label">{`${formatXAxis(label)}`}</p>
          <p className="label">{`${
            payload[0].name
          } : ${payload[0].value.toFixed(2)}`}</p>
          <p className="label">{`${
            payload[1].name
          } : ${payload[1].value.toFixed(2)}`}</p>
          <p className="label">{`${
            payload[2].name
          } : ${payload[2].value.toFixed(2)}`}</p>
        </div>
      );
    }

    return null;
  };
  const SOC = [...data3];
  console.log(SOC[SOC.length - 1]?.soc);
  console.log(data1, data2, data3);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/exhaustive-deps
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

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

              <div>{SOC[SOC.length - 1]?.soc?.toFixed(3)}%</div>
            </BatteryNow>
            <SOCChange>
              <div>배터리 상태 (State of Charge)</div>
              <LineChart
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
                <YAxis
                  type="number"
                  domain={["dataMin", "dataMax"]}
                  tickFormatter={formatYAxis}
                />
                <Tooltip content={<CustomTooltip />} />
                <Line
                  type="monotone"
                  dot={false}
                  dataKey="soc"
                  stroke="#8884d8"
                />
              </LineChart>
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
                <XAxis dataKey="timestamp" tickFormatter={formatXAxis} />
                <YAxis yAxisId="left" />
                <YAxis yAxisId="right" orientation="right" />
                <Tooltip content={<CustomTooltip2 />} />
                <Legend />
                <Line
                  yAxisId="right"
                  dot={false}
                  name="Battery Power"
                  type="monotone"
                  dataKey="batteryPower"
                  stroke="#8884d8"
                />
                <Line
                  yAxisId="left"
                  dot={false}
                  name="Consumption"
                  type="monotone"
                  dataKey="consumption"
                  stroke="red"
                />
                <Line
                  yAxisId="left"
                  dot={false}
                  name="TOU(Time of Usage)"
                  type="monotone"
                  dataKey="tou"
                  stroke="#82ca9d"
                />
              </LineChart>
            </ResponsiveContainer>
          </BatteryGraph>
          <BatteryGraph>
            ESS를 사용하여 감소시킨 전력소비량
            <ResponsiveContainer width="100%" height="100%">
              <LineChart
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
                <XAxis dataKey="timestamp" tickFormatter={formatXAxis} />
                <YAxis yAxisId="left" />
                <YAxis
                  type="number"
                  domain={["auto", "dataMax+0.5"]}
                  yAxisId="right"
                  orientation="right"
                />
                <Tooltip content={<CustomTooltip3 />} />
                <Legend />
                <Line
                  yAxisId="left"
                  name="기존 전력 소비량"
                  dot={false}
                  type="monotone"
                  dataKey="consumption"
                  stroke="#8884d8"
                />
                <Line
                  yAxisId="right"
                  name="감소된 전력 소비량"
                  dot={false}
                  type="monotone"
                  dataKey="netLoad"
                  stroke="red"
                />
                <Line
                  yAxisId="right"
                  name="한계점"
                  dot={false}
                  type="monotone"
                  dataKey="threshold"
                  stroke="#82ca9d"
                />
              </LineChart>
            </ResponsiveContainer>
          </BatteryGraph>
        </BatteryBackground>
      </BackGround>
    </div>
  );
}
