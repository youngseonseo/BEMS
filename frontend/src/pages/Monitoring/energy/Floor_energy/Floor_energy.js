import axios from "axios";
import { useEffect, useState } from "react";
import UserNavigationBar from "../../../../components/NavBar/userNavbar";
import MainHeader from "../../../../components/MainHeader/header";
import {
  BillCont,
  GraphCont,
  Graph1Cont,
  Graph2Cont,
  BackGround,
  TextCont,
  ButtonPost,
} from "./FloorbillStyle";
import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";

export default function FloorBillEnergyPage() {
  const [time, setTime] = useState("");
  const [consumption, setConsumption] = useState([]);
  const [price, setPrice] = useState([]);
  const moment = require("moment");
  const token = localStorage.getItem("accessToken");
  const getBill = async () => {
    await axios
      .get("http://localhost:8080/api/bill", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        console.log(res);
        return res.data;
      })
      .then((res) => {
        console.log(res);
        setTime(res.standardTimeStamp);
        setConsumption(res.totalConsumption);
        setPrice(res.totalPrice);
      });
  };
  const formatYAxis = (tickItem) => tickItem.toLocaleString();
  const Consumdata = [
    {
      date: "2달전",
      elec_consumtion: consumption[0],
    },
    {
      date: "1달전",
      elec_consumtion: consumption[1],
    },
    {
      date: "이번달",
      elec_consumtion: consumption[2],
    },
  ];
  const PriceData = [
    {
      date: "2달전",
      elec_price: price[0],
    },
    {
      date: "1달전",
      elec_price: price[1],
    },
    {
      date: "이번달",
      elec_price: price[2],
    },
  ];
  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip" style={{ background: "white" }}>
          <p
            className="label"
            style={{ color: "black", fontWeight: "bold" }}
          >{`요금: ${payload[0].value.toLocaleString()}원`}</p>
        </div>
      );
    }

    return null;
  };
  const CustomToolti2 = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip" style={{ background: "white" }}>
          <p
            className="label"
            style={{ color: "black", fontWeight: "bold" }}
          >{`전력량: ${payload[0].value.toLocaleString()}kW`}</p>
        </div>
      );
    }

    return null;
  };
  useEffect(() => {
    getBill();
    // eslint-disable-next-line
  }, []);

  return (
    <div>
      <MainHeader />
      <BackGround>
        <UserNavigationBar name="bill" />

        <BillCont>
          <div
            style={{
              fontSize: "23px",
              padding: "0px 0px 40px 0px",
              fontWeight: "Bold",
            }}
          >
            현재까지 쓴 이번달 전기 요금은 {price[2]?.toLocaleString()}원
            입니다.
          </div>
          <TextCont>
            <div> 3달 간 전력 사용량 </div>
            <div>
              {moment(time).format("YYYY-MM-DD HH시 mm분") + " 기준  "}
              <ButtonPost onClick={getBill}>Check</ButtonPost>
            </div>
          </TextCont>
          <GraphCont>
            <Graph1Cont>
              전력요금(원)
              <BarChart
                width={400}
                height={350}
                data={PriceData}
                margin={{ top: 20, bottom: 20, left: 25 }}
              >
                <Tooltip content={<CustomTooltip />} />
                <XAxis dataKey="date" stroke="white" />
                <YAxis stroke="white" tickFormatter={formatYAxis} />
                <Bar dataKey="elec_price" fill="#8884d8" barSize={40} />
              </BarChart>
            </Graph1Cont>
            <Graph2Cont>
              전력 사용량 (kw)
              <BarChart
                width={400}
                height={350}
                data={Consumdata}
                margin={{ top: 20, bottom: 20, left: 10 }}
              >
                <XAxis dataKey="date" stroke="white" />
                <YAxis stroke="white" tickFormatter={formatYAxis} />
                <Tooltip content={<CustomToolti2 />} />
                <Bar dataKey="elec_consumtion" fill="#f7d954" barSize={40} />
              </BarChart>
            </Graph2Cont>
          </GraphCont>
        </BillCont>
      </BackGround>
    </div>
  );
}
