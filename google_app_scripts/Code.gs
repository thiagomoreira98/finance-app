function doGet(e) {
  const action = e.parameter.action;
  if (action === "getAllData") {
    return respondJSON(getAllData());
  }
  return respondJSON({ status: "error", message: "Ação não encontrada" });
}

function doPost(e) {
  try {
    const data = JSON.parse(e.postData.contents);
    const action = data.action;

    switch(action) {
      case "addCreditCard":
        return respondJSON(addItem("Cartões", data.payload));
      case "updateCreditCard":
        return respondJSON(updateItem("Cartões", data.payload));
      case "deleteCreditCard":
        return respondJSON(deleteItem("Cartões", data.payload.id));

      case "addCreditCardItem":
        return respondJSON(addItem("Cartão de Crédito", data.payload));
      case "updateCreditCardItem":
        return respondJSON(updateItem("Cartão de Crédito", data.payload));
      case "deleteCreditCardItem":
        return respondJSON(deleteItem("Cartão de Crédito", data.payload.id));

      case "addFixedCostItem":
        return respondJSON(addItem("Custos Fixos", data.payload));
      case "updateFixedCostItem":
        return respondJSON(updateItem("Custos Fixos", data.payload));
      case "deleteFixedCostItem":
        return respondJSON(deleteItem("Custos Fixos", data.payload.id));

      case "addIncomeItem":
        return respondJSON(addItem("Rendas", data.payload));
      case "updateIncomeItem":
        return respondJSON(updateItem("Rendas", data.payload));
      case "deleteIncomeItem":
        return respondJSON(deleteItem("Rendas", data.payload.id));

      default:
        return respondJSON({ status: "error", message: "Ação inválida" });
    }
  } catch (error) {
    return respondJSON({ status: "error", message: error.toString() });
  }
}

function setupSheets() {
  const ss = SpreadsheetApp.getActiveSpreadsheet();
  
  // Create sheets if not present
  const sheets = [
    { name: "Cartões", headers: ["id", "name", "lastFourDigits", "type"] },
    { name: "Cartão de Crédito", headers: ["id", "nome", "data", "valorTotal", "parcelas", "cartao"] },
    { name: "Custos Fixos", headers: ["id", "nome", "valor", "diaVencimento", "categoria"] },
    { name: "Rendas", headers: ["id", "origem", "valor", "diaRecebimento"] }
  ];

  sheets.forEach(s => {
    let sheet = ss.getSheetByName(s.name);
    if (!sheet) {
      sheet = ss.insertSheet(s.name);
      sheet.appendRow(s.headers);
    }
  });
}

function getAllData() {
  return {
    cards: getSheetData("Cartões"),
    creditCard: getSheetData("Cartão de Crédito"),
    fixedCosts: getSheetData("Custos Fixos"),
    incomes: getSheetData("Rendas")
  };
}

function getSheetData(sheetName) {
  const sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetName);
  if (!sheet) return [];
  const rows = sheet.getDataRange().getValues();
  if (rows.length <= 1) return [];
  
  const headers = rows[0];
  return rows.slice(1).map(row => {
    let obj = {};
    headers.forEach((h, index) => {
      obj[h] = row[index];
    });
    return obj;
  });
}

function addItem(sheetName, item) {
  const sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetName);
  const headers = sheet.getDataRange().getValues()[0];
  if (!item.id) item.id = Utilities.getUuid();
  
  const row = headers.map(h => item[h] !== undefined ? item[h] : "");
  sheet.appendRow(row);
  return { status: "success", item: item };
}

function updateItem(sheetName, item) {
  const sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetName);
  const data = sheet.getDataRange().getValues();
  const headers = data[0];
  const idIndex = headers.indexOf("id");
  
  for (let i = 1; i < data.length; i++) {
    if (data[i][idIndex] === item.id) {
      const row = headers.map(h => item[h] !== undefined ? item[h] : data[i][headers.indexOf(h)]);
      sheet.getRange(i + 1, 1, 1, headers.length).setValues([row]);
      return { status: "success", item: item };
    }
  }
  return { status: "error", message: "Item não encontrado" };
}

function deleteItem(sheetName, id) {
  const sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetName);
  const data = sheet.getDataRange().getValues();
  const idIndex = data[0].indexOf("id");
  
  for (let i = 1; i < data.length; i++) {
    if (data[i][idIndex] === id) {
      sheet.deleteRow(i + 1);
      return { status: "success", id: id };
    }
  }
  return { status: "error", message: "Item não encontrado" };
}

function respondJSON(data) {
  return ContentService.createTextOutput(JSON.stringify(data))
    .setMimeType(ContentService.MimeType.JSON);
}
