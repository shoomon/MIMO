import { MileageStatusProps } from "@/components/atoms/MileageStatus/MileageStatus";
import { RawDataRow } from "@/utils/transformTableData";

export const TeamMileageMain:MileageStatusProps[] = [
  {
    type: "balance",
    amount: 1912750
  }, 
  {
    type: "income",
    amount: 5600
  }, 
  {
    type: "expense",
    amount: 3460
  }
]

// 외부에서 전달받은 raw 데이터 예시
export const rawTableData: RawDataRow[] = [
    {
        id: 1,
        transaction: '출금',
        name: '홍길동',
        date: '2025-02-06',
        amount: 10000,
        hasReceipt: true,
    },
    {
        id: 2,
        transaction: '입금',
        name: '김철수',
        date: '2025-02-07',
        amount: 20000,
        hasReceipt: false,
    },
];