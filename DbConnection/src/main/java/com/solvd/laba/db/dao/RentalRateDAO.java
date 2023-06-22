package com.solvd.laba.db.dao;

import com.solvd.laba.db.model.RentalRate;
import com.solvd.laba.db.util.GenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentalRateDAO extends AbstractDAO implements GenericDAO<RentalRate> {
    private static final Logger logger = LogManager.getLogger(RentalRateDAO.class);
    private final static String GET_BY_ID = "SELECT * FROM rental_rate WHERE id=?";
    private final static String GET_ALL = "SELECT * FROM rental_rate";
    private final static String INSERT_VALUES = "INSERT INTO rental_rate (rental_type, rental_amount, min_duration, max_duration) VALUES (?, ?, ?, ?)";
    private final static String UPDATE_VALUE = "UPDATE rental_rate SET rental_type=?, rental_amount=?, min_duration=?, max_duration=? WHERE id=?";
    private final static String DELETE_VALUE = "DELETE FROM rental_rate WHERE id=?";

    @Override
    public RentalRate get(int id) {
        logger.info("Finding record of ID: " + id + "...");
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(GET_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                int rentalId = result.getInt("id");
                String rentalType = result.getString("rental_type");
                double rentalAmount = result.getDouble("rental_amount");
                Integer minDuration = result.getInt("min_duration");
                Integer maxDuration = result.getInt("max_duration");
                RentalRate rentalRate = new RentalRate(rentalId, rentalType, rentalAmount, minDuration, maxDuration);
                logger.info(rentalRate);
                return rentalRate;
            }
            logger.info("No record found. Invalid ID");
        } catch (SQLException e) {
            logger.error("Error executing SQL query:" + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<RentalRate> get() {
        logger.info("Displaying all the rows from rental_rate table");
        ArrayList<RentalRate> rentalRates = new ArrayList<>();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(GET_ALL)) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                int rentalId = result.getInt("id");
                String rentalType = result.getString("rental_type");
                double rentalAmount = result.getDouble("rental_amount");
                Integer minDuration = result.getInt("min_duration");
                Integer maxDuration = result.getInt("max_duration");
                RentalRate rentalRate = new RentalRate(rentalId, rentalType, rentalAmount, minDuration, maxDuration);
                rentalRates.add(rentalRate);
            }
            logger.info(rentalRates);
            return rentalRates;
        } catch (SQLException e) {
            logger.error("Error executing SQL query");
        }
        return null;
    }

    @Override
    public void add(RentalRate rentalRate) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(INSERT_VALUES)) {
            preparedStatement.setString(1, rentalRate.getRentalType());
            preparedStatement.setDouble(2, rentalRate.getRentalAmount());
            preparedStatement.setInt(3, rentalRate.getMinDuration());
            preparedStatement.setInt(4, rentalRate.getMaxDuration());
            preparedStatement.executeUpdate();
            logger.info("Insertion complete");
        } catch (SQLException e) {
            logger.error("Error executing SQL query:"+e.getMessage());
        }
    }

    @Override
    public void update(RentalRate rentalRate, int id) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_VALUE)) {
            preparedStatement.setString(1, rentalRate.getRentalType());
            preparedStatement.setDouble(2, rentalRate.getRentalAmount());
            preparedStatement.setInt(3, rentalRate.getMinDuration());
            preparedStatement.setInt(4, rentalRate.getMaxDuration());
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
            logger.info("Update complete");
        } catch (SQLException e) {
            logger.error("Update error:"+e.getMessage());
            logger.error("Error! id is null. Use another non-null id");
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(DELETE_VALUE)) {
            if (get(id) != null) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                logger.info("Deletion complete");
            }
        } catch (SQLException e) {
            logger.error("Error executing SQL query:"+e.getMessage());
        }
    }
}
