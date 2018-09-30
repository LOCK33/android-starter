package net.bndy.ad.model;

import com.google.gson.annotations.SerializedName;

public class GoogleUser implements AppUserInteface {

    private String name;
    @SerializedName("given_name")
    private String givenName;
    @SerializedName("family_name")
    private String familyName;
    private String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;

    @Override
    public AppUser toAppUser() {
        AppUser appUser = new AppUser();
        appUser.setAvatar(this.getPicture());
        appUser.setName(this.getName());
        if (this.getGender() != null && !this.getGender().isEmpty()) {
            appUser.setMale("male".equals(this.getGender()));
        }
        return appUser;
    }
}
