package com.appointment.appointment;

import java.util.ArrayList;

public class Movie  {
        private String title;
        private String year;
        private String rating;


        public Movie() {
        }

        public Movie(String name,  String year, String rating,
                     ArrayList<String> genre) {
            this.title = name;

            this.year = year;
            this.rating = rating;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String name) {
            this.title = name;
        }





        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }





}
