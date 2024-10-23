package TradeInstructionsReport;

import Entity.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TradeInstructionsReport {
    
    
    public SettlementReport generateReport(List<TradeInstruction> tradeInstructions) {
        List<TradeInstructionCalcSet> tradeInstructionCalcSets = new ArrayList<>();
        for (TradeInstruction tradeInstruction : tradeInstructions) {
            tradeInstructionCalcSets.add(new TradeInstructionCalcSet(tradeInstruction));
        }
        LocalDate startDate = tradeInstructionCalcSets.get(0).getSettlementDate(), endDate =
                tradeInstructionCalcSets.get(0).getSettlementDate();
        System.out.println(endDate);
        for (TradeInstructionCalcSet tradeInstructionCalcSet : tradeInstructionCalcSets) {
            if (endDate.isBefore(tradeInstructionCalcSet.getSettlementDate())) {
                endDate = tradeInstructionCalcSet.getActualSettlementDate();
            }
            if (startDate.isAfter(tradeInstructionCalcSet.getSettlementDate())) {
                startDate = tradeInstructionCalcSet.getActualSettlementDate();
            }
        }
        List<LocalDate> listOfDates = startDate.datesUntil(endDate)
                .collect(Collectors.toList());
        listOfDates.add(startDate);
        List<SettlementRecord> settlementRecords = new ArrayList<>();
        for (LocalDate date : listOfDates) {
            Float incomingSettlementAmount = 0F, outgoingSettlementAmount = 0F;
            for (TradeInstructionCalcSet tradeInstructionCalcSet : tradeInstructionCalcSets) {
                if (date.isEqual(tradeInstructionCalcSet.getActualSettlementDate())) {
                    if (tradeInstructionCalcSet.getBuyOrSell().equals("B")) {
                        outgoingSettlementAmount += tradeInstructionCalcSet.getAmount();
                    } else if (tradeInstructionCalcSet.getBuyOrSell().equals("S")) {
                        incomingSettlementAmount += tradeInstructionCalcSet.getAmount();
                    } else {
                        System.out.println("Error - encountered unknown Buy Or Sell instruction during processing");
                    }
                }
            }
            settlementRecords.add(new SettlementRecord(outgoingSettlementAmount, incomingSettlementAmount, date));
        }

        List<EntityRecord> entityRanking = new ArrayList<>();
        Set<String> entitySet = new HashSet<>();
        for (TradeInstructionCalcSet tradeInstructionCalcSet : tradeInstructionCalcSets) {
            entitySet.add(tradeInstructionCalcSet.getEntity());
        }
        List<EntityWithAmount> outgoingEntityRanking = getRankedEntityWithAmountList(tradeInstructionCalcSets, "B");
        List<EntityWithAmount> incomingEntityRanking = getRankedEntityWithAmountList(tradeInstructionCalcSets, "S");

        return new SettlementReport(settlementRecords, outgoingEntityRanking, incomingEntityRanking);
    }

    private static List<EntityWithAmount> getRankedEntityWithAmountList(List<TradeInstructionCalcSet> tradeInstructionCalcSets, String buyOrSell) {
//        Map<String, List<TradeInstructionCalcSet>> instructionsPerEntity  = new HashMap<>();
//        for (TradeInstructionCalcSet tradeInstructionCalcSet : tradeInstructionCalcSets) {
//            if (tradeInstructionCalcSet.getBuyOrSell().equals(buyOrSell)) {
//                List<TradeInstructionCalcSet> instructions =
//                        instructionsPerEntity.putIfAbsent(tradeInstructionCalcSet.getEntity(), new ArrayList<>());
//                instructions.add(tradeInstructionCalcSet);
//            }
//        }
        Map<String, List<TradeInstructionCalcSet>> instructionsPerEntity =  tradeInstructionCalcSets.stream()
                .filter((instruction) -> instruction.getBuyOrSell().equals(buyOrSell))
                .collect(Collectors.groupingBy(instruction -> instruction.getEntity()));

        List<EntityWithAmount> entityWithAmountList = new ArrayList<>();
        instructionsPerEntity.forEach((entity, instructionCalcSetList) -> {
            Double totalAmount = instructionCalcSetList.stream()
                    .mapToDouble(instruction -> instruction.getAmount())
                    .sum();
            entityWithAmountList.add(new EntityWithAmount(entity, totalAmount));
        });
        Collections.sort(entityWithAmountList,
                         Comparator.comparingDouble((EntityWithAmount entityWithAmount) -> entityWithAmount.amount()).reversed());
        return entityWithAmountList;
    }
}