# Product Interest Rates
GET /api/products/{productCode}/interest-rates/{rateCode}
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

Like this we get the responses for 12M, 24M, 36M, and 60M

Note: if currency = INR and amount < 5,00,000 then productCode is FD001, if amount > 5,00,000 then productCode is FD002, accordingly for all the codes, in the ending suffix we replace 001 with 002, like that.

# Product Charges and Fees
This is the penalty for breakage of FD early (after 50 percent completion of tenure is PEN-L-001) (before 50 percent completion of tenure is PEN-H-001)

GET /api/products/{productCode}/charges/{chargeCode}
productCode: FD001
chargeCode: PEN-L-001

Response:
{
  "chargeId": "9953d914-423c-45af-936b-ebffbdd709be",
  "chargeCode": "PEN-L-001",
  "chargeName": "Penalty for FD breakage after 50% tenure complete",
  "chargeType": "PENALTY",
  "calculationType": "PERCENTAGE",
  "frequency": "ONE_TIME",
  "amount": 0.5,
  "debitCredit": "DEBIT"
}

For checking the fee for the account, lets say for FD001, we do FEE001 as chargeCode

Response:
{
  "chargeId": "31435394-2af5-4118-9cc7-4616df6a64ee",
  "chargeCode": "FEE001",
  "chargeName": "Maintenance fee",
  "chargeType": "FEE",
  "calculationType": "FLAT",
  "frequency": "QUARTERLY",
  "amount": 200,
  "debitCredit": "DEBIT"
}

# Product Communications
GET /api/products/{productCode}/communications/{commCode}
productCode: FD001
commCode: COMM_OPENING

Response:
{
  "commId": "e1946a5c-1979-4dfa-a721-302e7a89bc07",
  "commCode": "COMM_OPENING",
  "communicationType": "ALERT",
  "channel": "SMS",
  "event": "COMM_OPENING",
  "template": "Dear ${CUSTOMER_NAME}, welcome! Your new ${PRODUCT_NAME} account (${ACCOUNT_NUMBER}) was successfully opened on ${DATE}. If you did not authorize this account opening, contact us immediately.",
  "frequencyLimit": 0
}

# Product Transactions
GET /api/products/{productCode}/transactions/{transactionCode}
productCode: FD001
transactionCode: FD_DEPOSIT

Response:
{
  "transactionId": "3e6dcd5d-1615-4e93-8cd3-dc9a2bf8713f",
  "transactionCode": "FD_DEPOSIT",
  "transactionType": "DEPOSIT",
  "isAllowed": true
}

# Product Roles
GET /api/products/{productCode}/roles/{roleCode}
productCode: FD001
roleCode: ROLE001

Response:
{
  "roleId": "f1097ab9-789c-4bf2-984a-eaa6d42b1fed",
  "roleCode": "ROLE001",
  "roleType": "OWNER",
  "roleName": "OWNER",
  "isMandatory": true,
  "maxCount": 1
}




