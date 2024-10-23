package Entity;import java.util.List;public class SettlementReport {    private List<SettlementRecord> settlementRecords;    private List<EntityWithAmount> outgoingEntityRanking;    private List<EntityWithAmount> incomingEntityRanking;    public SettlementReport() {    }    public SettlementReport(List<SettlementRecord> settlementRecords, List<EntityWithAmount> outgoingEntityRanking,                            List<EntityWithAmount> incomingEntityRanking) {        this.settlementRecords = settlementRecords;        this.outgoingEntityRanking = outgoingEntityRanking;        this.incomingEntityRanking = incomingEntityRanking;    }    public List<SettlementRecord> getSettlementRecords() {        return settlementRecords;    }    public void setSettlementRecords(List<SettlementRecord> settlementRecords) {        this.settlementRecords = settlementRecords;    }    public List<EntityWithAmount> getEntityRankingByIncoming() {        return outgoingEntityRanking;    }    public void setEntityRankingByIncoming(List<EntityWithAmount> outgoingEntityRanking) {        this.outgoingEntityRanking = outgoingEntityRanking;    }    public List<EntityWithAmount> getEntityRankingByOutgoing() {        return incomingEntityRanking;    }    public void setEntityRankingByOutgoing(List<EntityWithAmount> incomingEntityRanking) {        this.incomingEntityRanking = incomingEntityRanking;    }}