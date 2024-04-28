package com.example.wildlife_hms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPermissionsModel {

    private String userName;
    private boolean habitatManagement;

    private boolean userManagement;

    private boolean fieldDataCollection;

    private boolean research;

    private boolean reporting;

    private boolean settings;

    private  boolean otherFiles;

    private boolean create;

    private boolean delete;

    private boolean update;

    private boolean view;

    private int id;

    public UserPermissionsModel(int id,String userName, boolean habitatManagement, boolean userManagement, boolean fieldDataCollection, boolean research, boolean reporting, boolean settings, boolean otherFiles, boolean create, boolean delete, boolean update, boolean view) {
        this.id=id;
        this.userName = userName;
        this.habitatManagement = habitatManagement;
        this.userManagement = userManagement;
        this.fieldDataCollection = fieldDataCollection;
        this.research = research;
        this.reporting = reporting;
        this.settings = settings;
        this.otherFiles = otherFiles;
        this.create = create;
        this.delete = delete;
        this.update = update;
        this.view = view;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isHabitatManagement() {
        return habitatManagement;
    }

    public void setHabitatManagement(boolean habitatManagement) {
        this.habitatManagement = habitatManagement;
    }

    public boolean isUserManagement() {
        return userManagement;
    }

    public void setUserManagement(boolean userManagement) {
        this.userManagement = userManagement;
    }

    public boolean isFieldDataCollection() {
        return fieldDataCollection;
    }

    public void setFieldDataCollection(boolean fieldDataCollection) {
        this.fieldDataCollection = fieldDataCollection;
    }

    public boolean isResearch() {
        return research;
    }

    public void setResearch(boolean research) {
        this.research = research;
    }

    public boolean isReporting() {
        return reporting;
    }

    public void setReporting(boolean reporting) {
        this.reporting = reporting;
    }

    public boolean isSettings() {
        return settings;
    }

    public void setSettings(boolean settings) {
        this.settings = settings;
    }

    public boolean isOtherFiles() {
        return otherFiles;
    }

    public void setOtherFiles(boolean otherFiles) {
        this.otherFiles = otherFiles;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }


    public static UserPermissionsModel getUserPermissionsByUsername(String username) {
        UserPermissionsModel userPermissions = null;
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String query = "SELECT * FROM userpermissions WHERE username = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Extract permissions from the result set
                int id = resultSet.getInt("id");
                boolean habitatManagement = resultSet.getBoolean("HabitatManagement");
                boolean userManagement = resultSet.getBoolean("UserManagement");
                boolean fieldDataCollection = resultSet.getBoolean("FieldDataCollection");
                boolean research = resultSet.getBoolean("Research");
                boolean reporting = resultSet.getBoolean("Reporting");
                boolean settings = resultSet.getBoolean("Settings");
                boolean otherFiles = resultSet.getBoolean("OtherFiles");
                boolean createP = resultSet.getBoolean("CreateP");
                boolean deleteP = resultSet.getBoolean("DeleteP");
                boolean updateP = resultSet.getBoolean("UpdateP");
                boolean view = resultSet.getBoolean("View");

                // Create a UserPermissionsModel object with fetched permissions
                userPermissions = new UserPermissionsModel(id, username, habitatManagement, userManagement,fieldDataCollection,research,reporting,settings,otherFiles,createP,deleteP,updateP,view);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userPermissions;
    }
}
