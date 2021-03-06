package dao.weather;

import model.Moon;
import model.WeatherModel;
import mysql_connection.DataBaseConnection;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by AZagorskyi on 06.04.2017.
 */
public class WeatherDaoGetImpl implements WeatherDaoGet {
    private static Logger log = Logger.getLogger(WeatherDaoGetImpl.class.getName());

    public Map<Integer, WeatherModel> getLastWeatherForRegions(String sqlQuery) {
        Map<Integer, WeatherModel> mapWeather = null;
        try {
            mapWeather = new ConcurrentHashMap<Integer, WeatherModel>();
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                WeatherModel model = new WeatherModel();
                model.setWindSpeed(Integer.parseInt(result.getString("wind_speed")));
                model.setWindRout(Integer.parseInt(result.getString("wind_rout")));
                mapWeather.put(Integer.parseInt(result.getString("id_region")), model);
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException sql){
            log.info(sql.getSQLState());
            sql.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        return mapWeather;
    }

    public WeatherModel getLastWeatherByRegionId(String sqlQuery, String regionId){
        WeatherModel model = null;
        try {
            model = new WeatherModel();
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, regionId);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                model.setDate(result.getDate("time"));
                model.setWindSpeed(Integer.parseInt(result.getString("wind_speed")));
                model.setWindRout(Integer.parseInt(result.getString("wind_rout")));
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException sql){
            log.info(sql.getSQLState());
            sql.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        return model;
    }

    public WeatherModel getWeatherByProvinceIdAndDate(String sqlQuery, int regionId, Date date){
        WeatherModel model = null;
        try {
            model = new WeatherModel();
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, regionId);
            statement.setTimestamp(2, new Timestamp(date.getTime()));
            ResultSet result = statement.executeQuery();
            while (result.next()){
                model.setDate(result.getDate("time"));
                model.setWindSpeed(Integer.parseInt(result.getString("wind_speed")));
                model.setWindRout(Integer.parseInt(result.getString("wind_rout")));
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException sql){
            log.info(sql.getSQLState());
            sql.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        return model;
    }

    public ArrayList<Integer> getPressuresByRegionId(String sqlQuery, int regionId, Date date){
        ArrayList<Integer> pressures = null;
        Timestamp timeFrom = null;
        Timestamp timeTo = null;
        try {
            pressures = new ArrayList<Integer>();
            if (date != null){
                timeFrom = new Timestamp(date.getTime() - 259200000);
                timeTo = new Timestamp(date.getTime() + 86400000);
            } else {
                Calendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DAY_OF_YEAR, -3);
                timeFrom = new Timestamp(calendar.getTime().getTime());
                calendar.add(Calendar.DAY_OF_YEAR, 5);
                timeTo = new Timestamp(calendar.getTime().getTime());
            }
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, regionId);
            statement.setTimestamp(2, timeFrom);
            statement.setTimestamp(3, timeTo);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                pressures.add(Integer.parseInt(result.getString("pressure")));
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException sql){
            log.info(sql.getSQLState());
            sql.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        log.info("This collect setPressures");
        return pressures;
    }

//    public List<Integer> getPressuresByProvinceIdAndDate(String sqlQuery, int provinceId, Date date){
//        ArrayList<Integer> pressures = null;
//        try {
//            pressures = new ArrayList<Integer>();
//            Connection connection = DataBaseConnection.getConnection();
//            Timestamp timeFrom = new Timestamp((date.getTime()/1000) - 259200);
//            Timestamp timeTo = new Timestamp((date.getTime()/1000) + 86400);
//            PreparedStatement statement = connection.prepareStatement(sqlQuery);
//            statement.setInt(1, provinceId);
//            statement.setTimestamp(2, timeFrom);
//            statement.setTimestamp(3, timeTo);
//            ResultSet result = statement.executeQuery();
//            while (result.next()){
//                pressures.add(Integer.parseInt(result.getString("pressure")));
//            }
//            result.close();
//            statement.close();
//            connection.close();
//        } catch (SQLException sql){
//            log.info(sql.getSQLState());
//            sql.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            log.info(e.toString());
//            e.printStackTrace();
//        }
//        log.info("This collect setPressures");
//        return pressures;
//    }

    public Moon getLastMoonDate(String sqlQuery){
        Moon moon = null;
        try {
            moon = new Moon();
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                moon.setPhase(result.getInt("moon_phase"));
                moon.setCreateTime(result.getDate("time"));
                moon.setDistance(result.getFloat("distance"));
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException sql){
            log.info(sql.getSQLState());
            sql.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        return moon;
    }

    public int getMoonPhaseByDate(String sqlQuery, Date date){
        int moonPhase = -1;
        try {
            Connection connection = DataBaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setTimestamp(1, new Timestamp(date.getTime()));
            ResultSet result = statement.executeQuery();
            while (result.next()){
                moonPhase = result.getInt("moon_phase");
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException sql){
            log.info(sql.getSQLState());
            sql.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        return moonPhase;
    }
}