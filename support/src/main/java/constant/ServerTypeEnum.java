package constant;

import lombok.Getter;

@Getter
public enum ServerTypeEnum {

    INTERNAL("internal"),
    ADMIN("admin");

    private final String code;

    ServerTypeEnum(String code) {
        this.code = code;
    }
}
