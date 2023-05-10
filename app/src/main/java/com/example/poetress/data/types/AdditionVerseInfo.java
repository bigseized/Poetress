package com.example.poetress.data.types;

public class AdditionVerseInfo {
        Boolean isLiked;
        Integer numOfLikes;
        Integer numOfComment;

        public AdditionVerseInfo(Boolean isLiked, Integer numOfLikes, Integer numOfComment) {
                this.isLiked = isLiked;
                this.numOfLikes = numOfLikes;
                this.numOfComment = numOfComment;
        }


        public AdditionVerseInfo() {
                isLiked = false;
                numOfLikes = 0;
                numOfComment = 0;
        }

        public Boolean getLiked() {
                return isLiked;
        }

        public Integer getNumOfLikes() {
                return numOfLikes;
        }

        public Integer getNumOfComment() {
                return numOfComment;
        }

        public void setLiked(Boolean liked) {
                isLiked = liked;
        }

        public void setNumOfLikes(Integer numOfLikes) {
                this.numOfLikes = numOfLikes;
        }

        public void setNumOfComment(Integer numOfComment) {
                this.numOfComment = numOfComment;
        }
}
