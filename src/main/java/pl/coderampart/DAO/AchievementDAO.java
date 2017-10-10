package pl.coderampart.DAO;

import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class AchievementDAO extends AbstractDAO {

    private QuestDAO questDAO = new QuestDAO();
    private CodecoolerDAO codecoolerDAO = new CodecoolerDAO();

//    public ArrayList<Achievement> readAll() {
//        ArrayList<Achievement> achievementList = new ArrayList<>();
//
//        try {
//            Connection connection = this.connectToDataBase();
//            String query = "SELECT * FROM achievements;";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Achievement achievement = this.createAchievementFromResultSet(resultSet);
//                achievementList.add(achievement);
//            }
//            connection.close();
//        } catch (Exception e) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//        }
//
//        return achievementList;
//    }

    public void create(Achievement achievement) {

        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO achievements (quest_id, codecooler_id, creation_date, id) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, achievement);
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Achievement achievement) throws Exception {
        statement.setString(1, achievement.getQuest().getID());
        statement.setString(2, achievement.getCodecooler().getID());
        statement.setString(3, achievement.getCreationDate().toString());
        statement.setString(4, achievement.getID());

        return statement;
    }

//    private Item createAchievementFromResultSet(ResultSet resultSet) throws Exception {
//        String ID = resultSet.getString("id");
//        String quest_id = resultSet.getString("quest_id");
//        Quest quest = questDAO.getByID(quest_id);
//        String creationDate = resultSet.getString("creation_date");
//        LocalDate creationDateObject = LocalDate.parse(creationDate);
//        String codecooler_id = resultSet.getString("codecooler_id");
//        Codecooler codecooler = codecoolerDAO.getByID(codecooler_id);
//
//
//        return new Achievement(ID, quest, creationDateObject, codecooler);
//    }
}
