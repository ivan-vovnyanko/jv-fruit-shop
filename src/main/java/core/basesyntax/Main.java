package core.basesyntax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import core.basesyntax.operations.BalanceOperation;
import core.basesyntax.operations.PurchaseOperation;
import core.basesyntax.operations.SupplyOperation;
import core.basesyntax.operations.ReturnOperation;
import core.basesyntax.operations.Operation;
import core.basesyntax.service.WriteFile;
import core.basesyntax.service.ReadFromFile;
import core.basesyntax.service.ImportOperations;
import core.basesyntax.service.DoOperations;
import core.basesyntax.service.CreateExportInfo;
import core.basesyntax.service.impl.WriteCsvFileImpl;
import core.basesyntax.service.impl.ImportOperationsImpl;
import core.basesyntax.service.impl.DoOperationImpl;
import core.basesyntax.service.impl.CreateExportInfoImpl;
import core.basesyntax.service.impl.ReadFromCsvFileImpl;
import core.basesyntax.strategy.OperationStrategy;
import core.basesyntax.strategy.OperationStrategyImpl;

public class Main {
    public static void main(String[] args) {
        ReadFromFile fileReader = new ReadFromCsvFileImpl();
        List<String> importInfo = fileReader.readFromFile("src/main/resources/importFile.csv");
        ImportOperations importOperations = new ImportOperationsImpl();
        List<String[]> listOfOperations = importOperations.getOperations(importInfo);

        Map<String, Operation> operationMap = new HashMap<>();
        operationMap.put("s", new SupplyOperation());
        operationMap.put("r", new ReturnOperation());
        operationMap.put("b", new BalanceOperation());
        operationMap.put("p", new PurchaseOperation());
        OperationStrategy fruitOperationStrategy = new OperationStrategyImpl(operationMap);

        DoOperations doOperations = new DoOperationImpl(fruitOperationStrategy);
        doOperations.closeAllOperations(listOfOperations);
        CreateExportInfo exportReport = new CreateExportInfoImpl();
        String report = exportReport.createReport();
        WriteFile writeFile = new WriteCsvFileImpl();
        writeFile.writeNewFile(report, "src/main/resources/exportFile.csv");
    }
}
