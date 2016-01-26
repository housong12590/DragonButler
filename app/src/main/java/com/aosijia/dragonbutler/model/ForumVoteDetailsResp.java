package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * 邻里投票详情
 * Created by Jacky on 2015/12/28.
 * Version 1.0
 */
public class ForumVoteDetailsResp extends BaseResp {

    private ForumVote data;

    public ForumVote getData() {
        return data;
    }

    public void setData(ForumVote data) {
        this.data = data;
    }


    public static class ForumVote extends BaseForum{
        private VoteExtra extra;

        public VoteExtra getExtra() {
            return extra;
        }

        public void setExtra(VoteExtra extra) {
            this.extra = extra;
        }

        public static class VoteExtra  {
            private String participants_count;
            private boolean  open;
            private boolean voted;
            private List<Option> options;



            public String getParticipants_count() {
                return participants_count;
            }

            public void setParticipants_count(String participants_count) {
                this.participants_count = participants_count;
            }

            public boolean isOpen() {
                return open;
            }

            public void setOpen(boolean open) {
                this.open = open;
            }

            public boolean isVoted() {
                return voted;
            }

            public void setVoted(boolean voted) {
                this.voted = voted;
            }

            public List<Option> getOptions() {
                return options;
            }

            public void setOptions(List<Option> options) {
                this.options = options;
            }
        }

        public static class Option{
            private String votes_count;
            private String description;
            private String vote_option_id;

            public String getVotes_count() {
                return votes_count;
            }

            public void setVotes_count(String votes_count) {
                this.votes_count = votes_count;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getVote_option_id() {
                return vote_option_id;
            }

            public void setVote_option_id(String vote_option_id) {
                this.vote_option_id = vote_option_id;
            }
        }
    }
}
