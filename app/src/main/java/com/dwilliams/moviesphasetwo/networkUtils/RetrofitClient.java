package com.dwilliams.moviesphasetwo.networkUtils;

import com.dwilliams.moviesphasetwo.constants.Constants;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

        private static Retrofit retrofit = null;

//       BASE_URL = https://api.themoviedb.org/3/movie/;

        public static Retrofit getClient() {

            if (null == retrofit) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.MOVIEDB_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }

            return retrofit;
        }

    }
