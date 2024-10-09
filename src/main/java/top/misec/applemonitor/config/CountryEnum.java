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
    CN_HK("CN-HK", "https://www.apple.com/hk"),
    CN_MO("CN-MO", "https://www.apple.com/mo"),
    CN_TW("CN-TW", "https://www.apple.com/tw"),
    JP("JP", "https://www.apple.com/jp"),
    KR("KR", "https://www.apple.com/kr"),
    SG("SG", "https://www.apple.com/sg"),
    MY("MY", "https://www.apple.com/my"),
    AU("AU", "https://www.apple.com/au"),
    UK("UK", "https://www.apple.com/uk"),
    CA("CA", "https://www.apple.com/ca"),
    US("US", "https://www.apple.com"),

    ;

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
