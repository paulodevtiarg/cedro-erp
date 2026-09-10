package br.com.dpsistemas.cedroerp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dyl8tkovm",
            "api_key", "779977573922135",
            "api_secret", "XBksdln_L8IZd-4qEvQnty8lnMs"
        ));
    }
}