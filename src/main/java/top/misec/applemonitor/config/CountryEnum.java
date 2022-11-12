package top.misec.applemonitor.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author moshi
 */
@Getter
@AllArgsConstructor
public enum CountryEnum {
    /**
     * Country，WebsiteURL
     */
    CN("CN", "https://www.apple.com.cn"),
    JP("JP", "https://www.apple.com/jp");

    final String country;
    final String url;

    public static String getUrlByCountry(String country) {
        for (CountryEnum countryEnum : CountryEnum.values()) {
            if (countryEnum.getCountry().equals(country)) {
                return countryEnum.getUrl();
            }
        }
        return CN.url;
    }
}
