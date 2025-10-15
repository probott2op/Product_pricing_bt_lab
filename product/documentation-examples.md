#Product Interest Rates
GET /api/products/{productCode}/interest-rates/rateCode
productCode: FD001
rateCode: INT12M001

Response:
{
  "rateId": "e75bed67-db57-405d-9f35-0af9d0d62e70",
  "rateCode": "INT12M001",
  "termInMonths": 12,
  "rateCumulative": 7.6,
  "rateNonCumulativeMonthly": 7.4,
  "rateNonCumulativeQuarterly": 7.5,
  "rateNonCumulativeYearly": 7.6
}

Like this we get the responses for 12M,24M,36M, and 60M

Note: if currency = INR and amount < 5,00,000 then productCode is FD001, if amount > 5,00,000 then productCode is FD002, accordingly for all the codes, in the ending suffix we replace 001 with 002, like that.

# Product charges and Fees
This is the penalty for breakage of FD early (after 50 percent completion of tenure is PEN-L-001) (before 50 percent completion of tenure is PEN-H-001)

GET /api/products/{productCode}/charges/{chargeCode}
productCode: FD001
rateCode: PEN-L-001

Response:
{
  "chargeId": "9953d914-423c-45af-936b-ebffbdd709be",
  "chargeCode": "PEN-L-001",
  "chargeType": "PENALTY",
  "calculationType": "PERCENTAGE",
  "frequency": "ONE_TIME",
  "amount": 0.5
}

For checking the fee for the account, lets say for FD001, we do FEE001 as chargeCode

Response:
{
  "chargeId": "31435394-2af5-4118-9cc7-4616df6a64ee",
  "chargeCode": "FEE001",
  "chargeType": "FEE",
  "calculationType": "FLAT",
  "frequency": "QUARTERLY",
  "amount": 200
}




