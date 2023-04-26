package com.hihonor.ads.group.sdk.base.net;

import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class ApiCallAdapterFactory extends CallAdapter.Factory {

    private ApiCallAdapterFactory() {
    }

    public static ApiCallAdapterFactory create() {
        return new ApiCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType != ApiCall.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new ApiCallAdapter<>(responseType, annotations);
    }

    public class ApiCallAdapter<R> implements CallAdapter<ApiResult<R>, ApiCall<R>> {

        private final Type responseType;
        private final Annotation[] mAnnotations;

        ApiCallAdapter(Type responseType, Annotation[] annotations) {
            this.responseType = responseType;
            this.mAnnotations = annotations;
        }

        @NonNull
        @Override
        public Type responseType() {
            return new ParameterizedTypeImpl(new Type[]{responseType}, null, ApiResult.class);
        }

        @NonNull
        @Override
        public ApiCall<R> adapt(@NonNull Call<ApiResult<R>> call) {
            return new ApiCall<>( call);
        }
    }
}
