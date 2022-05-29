package com.example.thread;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FetchProfileDataFromNSourcesAsync {

    ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        Instant start = Instant.now();
        FetchProfileDataFromNSourcesAsync async = new FetchProfileDataFromNSourcesAsync();
        List<Profile> profiles = async.fetchUserProfiles(Arrays.asList("andrew", "billy", "charlie", "david", "emma"));
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Time elapsed " + timeElapsed);
        System.out.println("Profiles " + profiles);
        System.exit(0);
    }

    List<Profile> fetchUserProfiles(List<String> profileIds){
        List<CompletableFuture<Profile>> future = profileIds.stream()
            .map((s) -> fetchUserProfileAsync(s))
            .collect(Collectors.toList());
        return future.stream().map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    CompletableFuture<Profile> fetchUserProfileAsync(String profileId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("fetchUserProfile " + Thread.currentThread().getName());
            List<Object> result = Stream.of(fetchBioOverHttpAsync(profileId), fetchPicturesFromS3BucketAsync(profileId), fetchDocumentsFromFtpServerAsync(profileId))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
            return new Profile((Bio)result.get(0), (List<String>) result.get(1), (List<String>) result.get(2));
        }, executor);
    }

    CompletableFuture<Bio> fetchBioOverHttpAsync(String profileId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("fetchBioOverHttpAsync " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Thread.sleep to assume it takes 1 sec to fetch bio
            // Here write code to fetch bio from API
            Random random = new Random();
            List<String> gender = Arrays.asList("male", "female", "na");
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            Collections.shuffle(gender);
            return new Bio(profileId, random.nextInt(100), gender.get(0), "location " + alphabet[random.nextInt(25)+1]);
        }, executor);
    }

    CompletableFuture<List<String>> fetchPicturesFromS3BucketAsync(String profileId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("fetchPicturesFromS3BucketAsync " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Thread.sleep to assume it takes 2 sec to fetch pictures
            // Here write code to fetch pictures from S3 bucket
            return Arrays.asList("picture of " + profileId);
        }, executor);
    }

    CompletableFuture<List<String>> fetchDocumentsFromFtpServerAsync(String profileId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("fetchDocumentsFromFtpServerAsync " + Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Thread.sleep to assume it takes 5 sec to fetch documents
            // Here write code to fetch documents from FTP server
            return Arrays.asList("document of " + profileId);
        }, executor);
    }
}

class Profile {
    Bio bio;
    List<String> pictures;
    List<String> documents;

    public Profile(Bio bio, List<String> pictures, List<String> documents) {
        this.bio = bio;
        this.pictures = pictures;
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "Profile{" +
            "bio=" + bio +
            ", pictures=" + pictures +
            ", documents=" + documents +
            '}';
    }
}

class Bio {
    String name;
    Integer age;
    String gender;
    String location;

    public Bio(String name, Integer age, String gender, String location) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Bio{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", gender='" + gender + '\'' +
            ", location='" + location + '\'' +
            '}';
    }
}
