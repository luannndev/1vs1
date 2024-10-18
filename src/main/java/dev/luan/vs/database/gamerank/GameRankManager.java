package dev.luan.vs.database.gamerank;

import dev.luan.vs.VS;
import dev.luan.vs.database.stats.StatsManager;
import dev.luan.vs.messages.ServerMessage;
import lombok.Getter;

import java.util.UUID;

public class GameRankManager {

    private final VS plugin;

    public GameRankManager(final VS plugin) {
        this.plugin = plugin;
    }

    public GameRank getPlayersGameRankType(final UUID uuid, final StatsManager.StatPeriod statPeriod, final String statKey) {
        final int rankPoints = this.plugin.getStatsManager().getStat(uuid, statPeriod, statKey);
        if(rankPoints < 0) return GameRank.UNRATED;
        GameRank finalGameRank = GameRank.UNRATED;
        for(final GameRank gameRank : GameRank.values()) {
            if(rankPoints < gameRank.getWins()) continue;
            finalGameRank = gameRank;
        }
        return finalGameRank;
    }

    public GameRank getGameRankByWins(final int value) {
        GameRank finalGameRank = GameRank.UNRATED;
        for(final GameRank gameRank : GameRank.values()) {
            if(value < gameRank.getWins()) continue;
            finalGameRank = gameRank;
        }
        return finalGameRank;
    }

    @Getter
    public enum GameRank {

        UNRATED("unrated", "Unrated", "§cUnrated", "", -1),

        BRONZE_I("bronze_i", ServerMessage.getString("<FF3A3A>Bronze I<E44C4C>"), ServerMessage.getString("<FF3A3A>◆ I<E44C4C>"), "", 0),
        BRONZE_II("bronze_ii", ServerMessage.getString("<FF3A3A>Bronze II<E44C4C>"), ServerMessage.getString("<FF3A3A>◆ II<E44C4C>"), "", 10),
        BRONZE_III("bronze_iii", ServerMessage.getString("<FF3A3A>Bronze III<E44C4C>"), ServerMessage.getString("<FF3A3A>◆ III<E44C4C>"), "", 20),

        SILVER_I("silver_i", ServerMessage.getString("<717171>Silver I<A2A2A2>"), ServerMessage.getString("<717171>◆ I<A2A2A2>"), "", 40),
        SILVER_II("silver_ii", ServerMessage.getString("<717171>Silver II<A2A2A2>"), ServerMessage.getString("<717171>◆ II<A2A2A2>"), "", 60),
        SILVER_III("silver_iii", ServerMessage.getString("<717171>Silver III<A2A2A2>"), ServerMessage.getString("<717171>◆ III<A2A2A2>"), "", 80),

        GOLD_I("gold_i", ServerMessage.getString("<FFB819>Gold I<FFF02D>"), ServerMessage.getString("<FFB819>◆ I<FFF02D>"), "", 100),
        GOLD_II("gold_ii", ServerMessage.getString("<FFB819>Gold II<FFF02D>"), ServerMessage.getString("<FFB819>◆ II<FFF02D>"), "", 130),
        GOLD_III("gold_iii", ServerMessage.getString("<FFB819>Gold III<FFF02D>"), ServerMessage.getString("<FFB819>◆ III<FFF02D>"), "", 160),

        EMERALD_I("emerald_i", ServerMessage.getString("<53FF6A>Emerald I<5EFFD3>"), ServerMessage.getString("<53FF6A>◆ I<5EFFD3>"), "", 200),
        EMERALD_II("emerald_ii", ServerMessage.getString("<53FF6A>Emerald II<5EFFD3>"), ServerMessage.getString("<53FF6A>◆ II<5EFFD3>"), "", 250),
        EMERALD_III("emerald_iii", ServerMessage.getString("<53FF6A>Emerald III<5EFFD3>"), ServerMessage.getString("<53FF6A>◆ III<5EFFD3>"), "", 300),

        CHAMPIONS("champions", ServerMessage.getString("<5440F1>Champions<D929CD>"), ServerMessage.getString("<5440F1>◆<D929CD>"), "", 450);


        String key, name, shortName, pointsRange;
        int wins;
        GameRank(String key, String name, String shortName, String pointsRange, int wins) {
            this.key = key;
            this.name = name;
            this.shortName = shortName;
            this.pointsRange = pointsRange;
            this.wins = wins;
        }
    }
}
