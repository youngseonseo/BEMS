import { useParams } from "react-router-dom";
import axios from "axios";
import { useEffect, useState } from "react";
import UserNavigationBar from "../../../../components/NavBar/userNavbar";
import MainHeader from "../../../../components/MainHeader/header";
import {
  BackGround,
  MangementBackground,
  ConsumptionPattern,
  ConsumptionPattern2,
  DayGraph,
  TextPattern,
  BatterySave,
  SaveBox,
  CompareCost,
  ColorChart,
  Checker,
  PercentText,
  CheckPosition,
  AvgPosition,
  ImageRotate,
  TextPercent,
  Subtitle,
} from "./EnergyManagementStyle";
import { PieChart, Pie, Sector, Cell, Tooltip, Label } from "recharts";

export default function EnergyManagementUserPage() {
  const [time, setTime] = useState("");
  const [consumption, setConsumption] = useState([]);
  const [saveCost, setSaveCost] = useState([]);
  const [compare, setCompare] = useState([]);
  const moment = require("moment");
  const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042"];
  const token = localStorage.getItem("accessToken");
  const getMangement = async () => {
    await axios
      .get("http://localhost:8080/api/energy/pattern", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        console.log(res);
        return res.data;
      })
      .then((res) => {
        console.log(res);
        setTime(res?.standardTimestamp);
        setConsumption(res?.energyConsumptionDto);
        setSaveCost(res?.energySaveCostDto);
        setCompare(res?.energyCompareDto);
      });
  };
  const RADIAN = Math.PI / 180;
  const renderCustomizedLabel = ({
    cx,
    cy,
    midAngle,
    innerRadius,
    outerRadius,
    startAngle,
    endAngle,
    fill,
    payload,
    percent,
    value,
    name,
  }) => {
    const radius = innerRadius + (outerRadius - innerRadius) * 0.2;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);
    const sin = Math.sin(-RADIAN * midAngle);
    const cos = Math.cos(-RADIAN * midAngle);
    const sx = cx + (outerRadius + 15) * cos;
    const sy = cy + (outerRadius + 15) * sin;
    const mx = cx + (outerRadius + 30) * cos;
    const my = cy + (outerRadius + 30) * sin;
    const ex = mx + (cos >= 0 ? 1 : -1) * 22;
    const ey = my;
    const textAnchor = cos >= 0 ? "start" : "end";

    return (
      <g>
        <text x={cx} y={cy} dy={8} textAnchor="middle" fill="white">
          {`총 소비량 `}
        </text>
        <Sector
          cx={cx}
          cy={cy}
          innerRadius={innerRadius}
          outerRadius={outerRadius}
          startAngle={startAngle}
          endAngle={endAngle}
          fill={fill}
        />
        <Sector
          cx={cx}
          cy={cy}
          startAngle={startAngle}
          endAngle={endAngle}
          innerRadius={outerRadius + 5}
          outerRadius={outerRadius + 7}
          fill={fill}
        />
        <path
          d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`}
          stroke={fill}
          fill="none"
        />
        <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none" />
        <text
          x={ex + (cos >= 0 ? 1 : -1) * 12}
          y={ey}
          textAnchor={textAnchor}
          dominantBaseline="central"
          fill="white"
        >{`${name}`}</text>

        <text
          x={x}
          y={y}
          fill="black"
          textAnchor={x > cx ? "start" : "end"}
          dominantBaseline="central"
        >
          {`${value.toFixed(0)}%`}
        </text>
      </g>
    );
  };
  const dataPattern = [
    { name: "오전", value: consumption.morningPercent },
    { name: "오후", value: consumption.afternoonPercent },
    { name: "저녁", value: consumption.eveningPercent },
    { name: "심야", value: consumption.lateNightPercent },
  ];
  useEffect(() => {
    getMangement();
  }, []);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <UserNavigationBar name="energy_management" />
        <MangementBackground>
          <Subtitle>우리집 에너지 소비</Subtitle>
          <div style={{ alignSelf: "flex-end" }}> {time} 기준</div>

          <Subtitle>에너지 소비패턴 분석(1일전)</Subtitle>
          <ConsumptionPattern>
            <DayGraph>
              <PieChart width={370} height={270}>
                <Pie
                  data={dataPattern}
                  innerRadius={60}
                  outerRadius={100}
                  startAngle={90}
                  endAngle={-270}
                  label={renderCustomizedLabel}
                  fill="#8884d8"
                  paddingAngle={3}
                  dataKey="value"
                >
                  {dataPattern.map((entry, index) => (
                    <Cell
                      key={`cell-${index}`}
                      fill={COLORS[index % COLORS.length]}
                    />
                  ))}
                </Pie>
                <Label
                  value="Pages of my website"
                  offset={0}
                  position="center"
                />
                <Tooltip />
              </PieChart>
            </DayGraph>
            <TextPattern>
              에너지를 가장 많이 사용하는 시간대는
              <div>
                "{consumption?.mostComsumptionTimeStartTime}시 ~
                {consumption?.mostComsumptionTimeEndTime}시"
              </div>
              입니다.
            </TextPattern>
          </ConsumptionPattern>
          <ConsumptionPattern2>
            <div>
              <Subtitle>우리집 ESS 배터리 이용 절약 요금</Subtitle>
              <BatterySave>
                <li>
                  하루
                  <SaveBox>
                    - {saveCost?.daySaveCost?.toLocaleString()}원
                  </SaveBox>
                </li>
                <li>
                  일주일
                  <SaveBox>
                    - {saveCost?.weekSaveCost?.toLocaleString()}원
                  </SaveBox>
                </li>
                <li>
                  이번달
                  <SaveBox>
                    - {saveCost?.monthSaveCost?.toLocaleString()}원
                  </SaveBox>
                </li>
              </BatterySave>
            </div>
            <div>
              <Subtitle>우리집 소비현황</Subtitle>
              <CompareCost>
                <AvgPosition>
                  <div>아파트 평균</div>
                  <div>({compare.buildingAverage}kW)</div>
                </AvgPosition>

                <ColorChart>
                  <Checker></Checker>
                  <Checker></Checker>
                  <Checker></Checker>
                  <Checker></Checker>
                  <Checker></Checker>
                  <Checker></Checker>
                </ColorChart>
                <PercentText>
                  <div>-60%</div>
                  <div>-30%</div>
                  <div>0%</div>
                  <div>30%</div>
                  <div>60%</div>
                </PercentText>
                <CheckPosition post={compare.compareAveragePercent}>
                  <ImageRotate
                    src="/image/arrow.png"
                    width={"40px"}
                    height={"25px"}
                    alt="arrow"
                  />
                  <div>우리집 사용량</div>
                  <div>({compare.floorAverage}kW)</div>
                </CheckPosition>
                <TextPercent>
                  이번달 평균 ({compare.buildingAverage}kW)대비
                  {compare.compareAveragePercent < 0
                    ? ` ${compare.compareAveragePercent}% 더 적게 `
                    : ` ${compare.compareAveragePercent}% 더 많이 `}
                  사용하였습니다.
                </TextPercent>
              </CompareCost>
            </div>
          </ConsumptionPattern2>
        </MangementBackground>
      </BackGround>
    </div>
  );
}
