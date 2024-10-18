package dev.luan.vs.database.stats;

import dev.luan.vs.database.DatabaseManager;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StatsManager {

    private final DatabaseManager databaseManager;
    private final String tableName;

    private boolean statResetted = false, seasonalStatResetted = false;
    public StatsManager(final DatabaseManager databaseManager, final String tableName) {
        this.databaseManager = databaseManager;
        this.tableName = tableName;
        if(this.databaseManager.getConnection() != null) {
            this.databaseManager.createTable("CREATE TABLE IF NOT EXISTS `" + tableName + "` (\n" +
                    " `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
                    " `uuid` char(255) NOT NULL,\n" +
                    " `player_name` char(255) NOT NULL,\n" +
                    " `" + StatPeriod.ALLTIME.getKey() + "` text NOT NULL,\n" +
                    " `" + StatPeriod.MONTHLY.getKey() + "` text NOT NULL,\n" +
                    " `" + StatPeriod.WEEKLY.getKey() + "` text NOT NULL,\n" +
                    " `" + StatPeriod.DAILY.getKey() + "` text NOT NULL,\n" +
                    " `" + StatPeriod.SEASONAL.getKey() + "` text NOT NULL" +

                    ") ", tableName);
        }
    }

    public void checkTemporaryStats() {
        if(this.databaseManager.getConnection() == null) return;
        final Date date = new Date(System.currentTimeMillis());
        DateFormat hourFormat = new SimpleDateFormat("HH");
        final String hours = hourFormat.format(date);

        this.checkSeasonalStats();
        if(hours.equalsIgnoreCase("00")) {
            if(this.statResetted) return;
            this.statResetted = true;

            for(final UUID uuid : this.getRegisteredPlayers()) {
                this.setStringValue(uuid, this.getPlayerName(uuid), StatPeriod.DAILY.getKey(), "");
            }
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            final int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
            if(day_of_week == 2) {
                for(final UUID uuid : this.getRegisteredPlayers()) {
                    this.setStringValue(uuid, this.getPlayerName(uuid), StatPeriod.WEEKLY.getKey(), "");
                }
            }

            final int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
            if(day_of_month == 1) {
                for(final UUID uuid : this.getRegisteredPlayers()) {
                    this.setStringValue(uuid, this.getPlayerName(uuid), StatPeriod.MONTHLY.getKey(), "");
                }
            }
        } else {
            statResetted = false;
        }
    }

    public void checkSeasonalStats() {
        if(this.databaseManager.getConnection() == null) return;
        this.isInList(UUID.fromString("b9610e39-42dc-4ee9-8570-05725a0ff459"));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        final int year = calendar.get(Calendar.YEAR);

        final List<String> timeDates = Arrays.asList("31/03/" + year + " 23:59", "30/06/" + year + " 23:59", "30/09/" + year + " 23:59", "31/12/" + year + " 23:59");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        Date inputDate;
        try {
            for(final String timeDate : timeDates) {
                inputDate = simpleDateFormat.parse(timeDate);
                if(System.currentTimeMillis() >= inputDate.getTime() && System.currentTimeMillis() <= inputDate.getTime() + TimeUnit.SECONDS.toMillis(2)) {
                    if(this.seasonalStatResetted) return;
                    this.seasonalStatResetted = true;
                    for(final UUID uuid : this.getRegisteredPlayers()) {
                        this.setStringValue(uuid, this.getPlayerName(uuid), StatPeriod.SEASONAL.getKey(), "");
                    }
                } else {
                    this.seasonalStatResetted = false;
                }
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
    }

    public String getCurrentSeason() {
        String season = "Jan.-März.";

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        final int year = calendar.get(Calendar.YEAR);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date inputDate;
        try {
            if(System.currentTimeMillis() < simpleDateFormat.parse("31/03/" + year + " 23:59").getTime()) {
                season = "Januar-März";
            }
            else if(System.currentTimeMillis() < simpleDateFormat.parse("30/06/" + year + " 23:59").getTime()) {
                season = "April-Juni";
            }
            else if(System.currentTimeMillis() < simpleDateFormat.parse("30/09/" + year + " 23:59").getTime()) {
                season = "Juli-September";
            }
            else if(System.currentTimeMillis() < simpleDateFormat.parse("31/12/" + year + " 23:59").getTime()) {
                season = "Oktober-Dezember";
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return season + " " + year;
    }

    /*
        DATABASE MANAGER
     */
    public List<UUID> getRegisteredPlayers() {
        if(this.databaseManager.getConnection() == null) return new ArrayList<>();
        final List<UUID> registeredPlayers = new ArrayList<>();
        try {
            final Statement statement = this.databaseManager.getConnection().createStatement();
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + this.tableName);
            while (resultSet.next()) {
                registeredPlayers.add(UUID.fromString(resultSet.getString("uuid")));
            }
            statement.close();
        } catch (SQLException exception) {
            return new ArrayList<>();
        }
        return registeredPlayers;
    }

    public void insertPlayer(final UUID uuid, final String player_name) {
        if(this.databaseManager.getConnection() == null) return;
        if(isInList(uuid)) return;
        try {
            PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("INSERT INTO " + this.tableName + " (" +
                    "uuid," +
                    "player_name," +
                    StatPeriod.ALLTIME.getKey() + "," +
                    StatPeriod.MONTHLY.getKey() + "," +
                    StatPeriod.WEEKLY.getKey() + "," +
                    StatPeriod.DAILY.getKey() + "," +
                    StatPeriod.SEASONAL.getKey() + ") VALUE (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, player_name);
            preparedStatement.setString(3, "");
            preparedStatement.setString(4, "");
            preparedStatement.setString(5, "");
            preparedStatement.setString(6, "");
            preparedStatement.setString(7, "");
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException exception) {
            this.databaseManager.disconnect();
            this.databaseManager.connect();
            this.insertPlayer(uuid, player_name);
        }
    }

    public boolean isInList(final UUID uuid) {
        if(this.databaseManager.getConnection() == null) return false;
        try {
            final PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("SELECT EXISTS (SELECT 1 FROM " + this.tableName + " WHERE uuid=?)");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                boolean inList = resultSet.getBoolean(1);
                resultSet.close();
                preparedStatement.close();
                return inList;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            this.databaseManager.disconnect();
            this.databaseManager.connect();
            this.isInList(uuid);
        }
        return false;
    }

    public boolean isInList(final String player_name) {
        if(this.databaseManager.getConnection() == null) return false;
        try {
            final PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("SELECT EXISTS (SELECT 1 FROM " + this.tableName + " WHERE player_name=?)");
            preparedStatement.setString(1, player_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                boolean inList = resultSet.getBoolean(1);
                resultSet.close();
                preparedStatement.close();
                return inList;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception) {
            this.databaseManager.disconnect();
            this.databaseManager.connect();
            this.isInList(player_name);
        }
        return false;
    }

    public String getStringValue(final UUID uuid, final String key) {
        try {
            try {
                PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("SELECT " + key + " FROM " + this.tableName + " WHERE uuid=?");
                preparedStatement.setString(1, uuid.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                String returnValue = null;
                if (resultSet.next()) {
                    returnValue = resultSet.getString(1);
                }
                resultSet.close();
                preparedStatement.close();
                return returnValue;
            } catch (SQLException e) {
                this.databaseManager.disconnect();
                this.databaseManager.connect();
                return getStringValue(uuid, key);
            }
        } catch (Exception e) {
            return "";
        }
    }

    public void setStringValue(final UUID uuid, final String player_name, final String key, final String value) {
        if(this.databaseManager.getConnection() == null) return;
        if(!isInList(uuid)) this.insertPlayer(uuid, player_name);
        try {
            PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("UPDATE " + this.tableName + " SET " + key + "=?, player_name=? WHERE uuid=?");
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, player_name);
            preparedStatement.setString(3, uuid.toString());
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("UPDATE " + this.tableName + " SET player_name=? WHERE uuid=?");
            preparedStatement.setString(1, player_name);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public UUID getUniqueId(final String player_name) {
        if(this.databaseManager.getConnection() == null) return null;
        try {
            PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("SELECT uuid FROM " + this.tableName + " WHERE player_name=?");
            preparedStatement.setString(1, player_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            String returnValue = null;
            if (resultSet.next()) {
                returnValue = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
            return UUID.fromString(returnValue);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getPlayerName(final UUID uuid) {
        if(this.databaseManager.getConnection() == null) return null;
        try {
            PreparedStatement preparedStatement = this.databaseManager.getConnection().prepareStatement("SELECT player_name FROM " + this.tableName + " WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            String returnValue = null;
            if (resultSet.next()) {
                returnValue = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
            return returnValue;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /*
        STATS MANAGER
     */
    public Integer getTopRank(final UUID uuid, final StatPeriod statPeriod, final String statkey) {
        int topRank = -1;
        for(final Integer i : this.getTopPlayers(statPeriod, statkey).keySet()) {
            if(!this.getTopPlayers(statPeriod, statkey).get(i).equals(uuid)) continue;
            topRank = i;
        }
        return topRank;
    }

    public HashMap<Integer, UUID> getTopPlayers(final StatPeriod statPeriod, final String statKey) {
        final HashMap<Integer, UUID> finalTopPlayerDatas = new HashMap<>();
        final HashMap<UUID, Integer> topPlayerDatas = this.sortByValues(this.getStatDatasforTopPlayer(statPeriod, statKey));
        int rankNumber = 1;
        for(final UUID uuid : topPlayerDatas.keySet()) {
            finalTopPlayerDatas.put(rankNumber, uuid);
            ++rankNumber;
        }
        return finalTopPlayerDatas;
    }

    private HashMap<UUID, Integer> getStatDatasforTopPlayer(final StatPeriod statPeriod, final String statKey) {
        final HashMap<UUID, Integer> statDatas = new HashMap<>();
        for(final UUID uuid : this.getRegisteredPlayers()) {
            statDatas.put(uuid, this.getStat(uuid, statPeriod, statKey));
        }
        return statDatas;
    }

    private HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public String getPlayerKDR(final UUID uuid, final StatPeriod statPeriod) {
        final int kills = this.getStat(uuid, statPeriod, "kills");
        final int deaths = this.getStat(uuid, statPeriod, "deaths");
        final DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        if (kills == 0 || deaths == 0) {
            return "N/D";
        }
        return decimalFormat.format((double)kills / (double)deaths);
    }

    public Integer getStat(final UUID uuid, StatPeriod statPeriod, String stat) {
        if(this.databaseManager.getConnection() == null) return 0;
        int statValue = 0;
        final String statString = (this.getStringValue(uuid, statPeriod.getKey()));
        if(statString.isEmpty()) return 0;
        final HashMap<String, Integer> statValues = new HashMap<>();
        for(final String statData : statString.split(",")) {
            final String statKey = statData.split(";")[0];
            if(!statKey.equalsIgnoreCase(stat)) continue;
            try {
                statValue = Integer.parseInt(statData.split(";")[1]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return statValue;
    }

    public void setStat(final UUID uuid, final String player_name, String stat, int value) {
        if(this.databaseManager.getConnection() == null) return;
        final HashMap<StatPeriod, HashMap<String, Integer>> newStatDatas = new HashMap<>();
        for(final StatPeriod statPeriod : StatPeriod.values()) {
            final String statString = this.getStringValue(uuid, statPeriod.getKey());

            HashMap<String, Integer> statCache = new HashMap<>();
            if(!statString.isEmpty()) {
                for(final String statData : statString.split(",")) {
                    final String[] statKey = statData.split(";");
                    int finalstatValue = Integer.parseInt(statKey[1]);
                    statCache.put(statKey[0], finalstatValue);
                }
            }

            int finalstatValue = value;
            if(finalstatValue < 0) finalstatValue = 0;
            statCache.put(stat, finalstatValue);
            newStatDatas.put(statPeriod, statCache);
        }

        for(final StatPeriod statPeriod : newStatDatas.keySet()) {
            int current = 1;
            final StringBuilder stringBuilder = new StringBuilder();

            for(final String finalStatKey : newStatDatas.get(statPeriod).keySet()) {
                if(current == newStatDatas.get(statPeriod).size()) {
                    stringBuilder.append(finalStatKey + ";" + newStatDatas.get(statPeriod).get(finalStatKey));
                } else {
                    stringBuilder.append(finalStatKey + ";" + newStatDatas.get(statPeriod).get(finalStatKey)).append(",");
                }
                ++current;
            }

            if(!stringBuilder.toString().isEmpty()) {
                this.setStringValue(uuid, player_name, statPeriod.getKey(), stringBuilder.toString());
            }
        }
    }

    public void addStat(final UUID uuid, final String player_name, String stat, int value) {
        final HashMap<StatPeriod, HashMap<String, Integer>> newStatDatas = new HashMap<>();
        for(final StatPeriod statPeriod : StatPeriod.values()) {
            final String statString = this.getStringValue(uuid, statPeriod.getKey());

            HashMap<String, Integer> statCache = new HashMap<>();
            if(!statString.isEmpty()) {
                for(final String statData : statString.split(",")) {
                    final String[] statKey = statData.split(";");
                    int finalstatValue = Integer.parseInt(statKey[1]);
                    statCache.put(statKey[0], finalstatValue);
                }
            }

            int finalstatValue = statCache.getOrDefault(stat, 0) + value;
            if(finalstatValue < 0) finalstatValue = 0;
            statCache.put(stat, finalstatValue);
            newStatDatas.put(statPeriod, statCache);
        }

        for(final StatPeriod statPeriod : newStatDatas.keySet()) {
            int current = 1;
            final StringBuilder stringBuilder = new StringBuilder();

            for(final String finalStatKey : newStatDatas.get(statPeriod).keySet()) {
                if(current == newStatDatas.get(statPeriod).size()) {
                    stringBuilder.append(finalStatKey + ";" + newStatDatas.get(statPeriod).get(finalStatKey));
                } else {
                    stringBuilder.append(finalStatKey + ";" + newStatDatas.get(statPeriod).get(finalStatKey)).append(",");
                }
                ++current;
            }

            if(!stringBuilder.toString().isEmpty()) {
                this.setStringValue(uuid, player_name, statPeriod.getKey(), stringBuilder.toString());
            }
        }
    }

    public void removeStat(final UUID uuid, final String player_name, String stat, int value) {
        final HashMap<StatPeriod, HashMap<String, Integer>> newStatDatas = new HashMap<>();
        for(final StatPeriod statPeriod : StatPeriod.values()) {
            final String statString = this.getStringValue(uuid, statPeriod.getKey());

            HashMap<String, Integer> statCache = new HashMap<>();
            if(!statString.isEmpty()) {
                for(final String statData : statString.split(",")) {
                    final String[] statKey = statData.split(";");
                    int finalstatValue = Integer.parseInt(statKey[1]);
                    statCache.put(statKey[0], finalstatValue);
                }
            }

            int finalstatValue = statCache.getOrDefault(stat, 0) - value;
            if(finalstatValue < 0) finalstatValue = 0;
            statCache.put(stat, finalstatValue);
            newStatDatas.put(statPeriod, statCache);
        }

        for(final StatPeriod statPeriod : newStatDatas.keySet()) {
            int current = 1;
            final StringBuilder stringBuilder = new StringBuilder();

            for(final String finalStatKey : newStatDatas.get(statPeriod).keySet()) {
                if(current == newStatDatas.get(statPeriod).size()) {
                    stringBuilder.append(finalStatKey + ";" + newStatDatas.get(statPeriod).get(finalStatKey));
                } else {
                    stringBuilder.append(finalStatKey + ";" + newStatDatas.get(statPeriod).get(finalStatKey)).append(",");
                }
                ++current;
            }

            if(!stringBuilder.toString().isEmpty()) {
                this.setStringValue(uuid, player_name, statPeriod.getKey(), stringBuilder.toString());
            }
        }
    }

    @Getter
    public enum StatPeriod {
        ALLTIME("alltime", "Gesamte Stats", "a67d813ae7ffe5be951a4f41f2aa619a5e3894e85ea5d4986f84949c63d7672e"),
        MONTHLY("monthly", "Monatliche Stats", "49c45a24aaabf49e217c15483204848a73582aba7fae10ee2c57bdb76482f"),
        WEEKLY("weekly", "Wöchentliche Stats", "269ad1a88ed2b074e1303a129f94e4b710cf3e5b4d995163567f68719c3d9792"),
        DAILY("daily", "Tägliche Stats", "3193dc0d4c5e80ff9a8a05d2fcfe269539cb3927190bac19da2fce61d71"),
        SEASONAL("seasonal", "Saisonale Stats", "3e41c60572c533e93ca421228929e54d6c856529459249c25c32ba33a1b1517");

        final String key, displayName, textureData;
        StatPeriod(final String key, final String displayName, final String textureData) {
            this.key = key;
            this.displayName = displayName;
            this.textureData = textureData;
        }
    }
}
