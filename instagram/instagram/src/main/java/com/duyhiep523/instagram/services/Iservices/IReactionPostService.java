package com.duyhiep523.instagram.services.Iservices;

import jakarta.transaction.Transactional;

public interface IReactionPostService {

    void addReactionToPost(String userId, String postId);

    int getReactionCountByPost(String postId);
    boolean hasUserReactedToPost(String userId, String postId);
    @Transactional
    void removeReactionFromPost(String userId, String postId);
}
