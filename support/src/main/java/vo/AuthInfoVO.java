package vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Setter
@Getter
public class AuthInfoVO {

    public static final String AUTH_HEADER_KEY = "tngus_auth";

    private boolean isAuth;
    private Long userId;
}
