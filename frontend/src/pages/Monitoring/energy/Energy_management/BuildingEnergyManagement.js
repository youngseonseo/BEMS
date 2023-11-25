import { useEffect, useState } from "react";
import NavigationBar from "../../../../components/NavBar/navbar";
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

import axios from "axios";
import { PieChart, Pie, Sector, Cell, Tooltip, Label } from "recharts";
export default function BuildingEnergyManagementPage() {
  const [time, setTime] = useState("");
  const [consumption, setConsumption] = useState([]);
  const [saveCost561, setSaveCost561] = useState([]);
  const [saveCost562, setSaveCost562] = useState([]);
  const [saveCost563, setSaveCost563] = useState([]);
  const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042"];
  const token = localStorage.getItem("accessToken");
  const getBuildingMangement = async () => {
    await axios
      .get("http://localhost:8080/api/energy/pattern/building", {
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
        setSaveCost561(res?.energySaveCostDto1);
        setSaveCost562(res?.energySaveCostDto2);
        setSaveCost563(res?.energySaveCostDto3);
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
    getBuildingMangement();
  }, []);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="buildingmanagement" />
        <MangementBackground>
          <Subtitle style={{ fontSize: "25px" }}>에너지 관리 현황</Subtitle>

          <Subtitle
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <div>전체동 에너지 소비패턴 분석(1일전)</div>
            <div style={{ fontSize: "15px" }}> {time} 기준</div>
          </Subtitle>
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
                "{consumption?.mostConsumptionTimeStartTime}시 ~
                {consumption?.mostConsumptionTimeEndTime}시"
              </div>
              입니다.
            </TextPattern>
          </ConsumptionPattern>
          <ConsumptionPattern2>
            <div>
              <Subtitle>561동 절약 요금</Subtitle>
              <BatterySave>
                <div>
                  하루
                  <SaveBox>
                    - {saveCost561?.daySaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>

                <div>
                  일주일
                  <SaveBox>
                    - {saveCost561?.weekSaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>
                <div>
                  한달
                  <SaveBox>
                    - {saveCost561?.monthSaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>
              </BatterySave>
            </div>
            <div>
              <Subtitle>562동 절약 요금</Subtitle>
              <BatterySave>
                <div>
                  하루
                  <SaveBox>
                    - {saveCost562?.daySaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>

                <div>
                  일주일
                  <SaveBox>
                    - {saveCost562?.weekSaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>
                <div>
                  한달
                  <SaveBox>
                    - {saveCost562?.monthSaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>
              </BatterySave>
            </div>
            <div>
              <Subtitle>563동 절약 요금</Subtitle>
              <BatterySave>
                <div>
                  하루
                  <SaveBox>
                    - {saveCost563?.daySaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>
                <div>
                  일주일
                  <SaveBox>
                    - {saveCost563?.weekSaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>
                <div>
                  한달
                  <SaveBox>
                    - {saveCost563?.monthSaveCost?.toLocaleString()}원
                  </SaveBox>
                </div>
              </BatterySave>
            </div>
          </ConsumptionPattern2>
        </MangementBackground>
      </BackGround>
    </div>
  );
}
