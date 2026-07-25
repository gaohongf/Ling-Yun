package com.lingyun.base.rsm.validation;

import com.lingyun.base.rsm.annotation.RsmInfo;
import com.lingyun.base.rsm.RsmManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BaseValidationRsm implements RsmManager {

    @RsmInfo(template = "只能为false", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_ASSERTFALSE_MESSAGE = "{jakarta.validation.constraints.AssertFalse.message}";
    @RsmInfo(template = "只能为true", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_ASSERTTRUE_MESSAGE = "{jakarta.validation.constraints.AssertTrue.message}";
    @RsmInfo(template = "必须小于${inclusive == true ? '或等于' : ''}{value}", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_DECIMALMAX_MESSAGE = "{jakarta.validation.constraints.DecimalMax.message}";
    
    @RsmInfo(template = "必须大于${inclusive == true ? '或等于' : ''}{value}", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_DECIMALMIN_MESSAGE = "{jakarta.validation.constraints.DecimalMin.message}";
    
    @RsmInfo(template = "数字的值超出了允许范围(只允许在{integer}位整数和{fraction}位小数范围内)", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_DIGITS_MESSAGE = "{jakarta.validation.constraints.Digits.message}";
    
    @RsmInfo(template = "不是一个合法的电子邮件地址", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_EMAIL_MESSAGE = "{jakarta.validation.constraints.Email.message}";
    
    @RsmInfo(template = "需要是一个将来的时间", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_FUTURE_MESSAGE = "{jakarta.validation.constraints.Future.message}";
    
    @RsmInfo(template = "需要是一个将来或现在的时间", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_FUTUREORPRESENT_MESSAGE = "{jakarta.validation.constraints.FutureOrPresent.message}";
    
    @RsmInfo(template = "最大不能超过{value}", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_MAX_MESSAGE = "{jakarta.validation.constraints.Max.message}";
    
    @RsmInfo(template = "最小不能小于{value}", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_MIN_MESSAGE = "{jakarta.validation.constraints.Min.message}";
    
    @RsmInfo(template = "必须是负数", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_NEGATIVE_MESSAGE = "{jakarta.validation.constraints.Negative.message}";
    
    @RsmInfo(template = "必须是负数或零", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_NEGATIVEORZERO_MESSAGE = "{jakarta.validation.constraints.NegativeOrZero.message}";
    
    @RsmInfo(template = "不能为空", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_NOTBLANK_MESSAGE = "{jakarta.validation.constraints.NotBlank.message}";
    
    @RsmInfo(template = "不能为空", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_NOTEMPTY_MESSAGE = "{jakarta.validation.constraints.NotEmpty.message}";
    
    @RsmInfo(template = "不能为null", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_NOTNULL_MESSAGE = "{jakarta.validation.constraints.NotNull.message}";
    
    @RsmInfo(template = "必须为null", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_NULL_MESSAGE = "{jakarta.validation.constraints.Null.message}";
    
    @RsmInfo(template = "需要是一个过去的时间", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_PAST_MESSAGE = "{jakarta.validation.constraints.Past.message}";
    
    @RsmInfo(template = "需要是一个过去或现在的时间", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_PASTORPRESENT_MESSAGE = "{jakarta.validation.constraints.PastOrPresent.message}";
    
    @RsmInfo(template = "需要匹配正则表达式\"{regexp}\"", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_PATTERN_MESSAGE = "{jakarta.validation.constraints.Pattern.message}";
    
    @RsmInfo(template = "必须是正数", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_POSITIVE_MESSAGE = "{jakarta.validation.constraints.Positive.message}";
    
    @RsmInfo(template = "必须是正数或零", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_POSITIVEORZERO_MESSAGE = "{jakarta.validation.constraints.PositiveOrZero.message}";
    
    @RsmInfo(template = "个数必须在{min}和{max}之间", status = HttpStatus.BAD_REQUEST)
    public static final String JAKARTA_VALIDATION_CONSTRAINTS_SIZE_MESSAGE = "{jakarta.validation.constraints.Size.message}";
    
    @RsmInfo(template = "不合法的信用卡号码", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_CREDITCARDNUMBER_MESSAGE = "{org.hibernate.validator.constraints.CreditCardNumber.message}";
    
    @RsmInfo(template = "不合法的货币 (必须是{value}其中之一)", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_CURRENCY_MESSAGE = "{org.hibernate.validator.constraints.Currency.message}";
    
    @RsmInfo(template = "不合法的{type}条形码", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_EAN_MESSAGE = "{org.hibernate.validator.constraints.EAN.message}";
    
    @RsmInfo(template = "不是一个合法的电子邮件地址", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_EMAIL_MESSAGE = "{org.hibernate.validator.constraints.Email.message}";
    
    @RsmInfo(template = "长度需要在{min}和{max}之间", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_LENGTH_MESSAGE = "{org.hibernate.validator.constraints.Length.message}";
    
    @RsmInfo(template = "长度需要在{min}和{max}之间", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_CODEPOINTLENGTH_MESSAGE = "{org.hibernate.validator.constraints.CodePointLength.message}";
    
    @RsmInfo(template = "${validatedValue}的校验码不合法, Luhn模10校验和不匹配", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_LUHNCHECK_MESSAGE = "{org.hibernate.validator.constraints.LuhnCheck.message}";
    
    @RsmInfo(template = "${validatedValue}的校验码不合法, 模10校验和不匹配", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_MOD10CHECK_MESSAGE = "{org.hibernate.validator.constraints.Mod10Check.message}";
    
    @RsmInfo(template = "${validatedValue}的校验码不合法, 模11校验和不匹配", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_MOD11CHECK_MESSAGE = "{org.hibernate.validator.constraints.Mod11Check.message}";
    
    @RsmInfo(template = "${validatedValue}的校验码不合法, {modType}校验和不匹配", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_MODCHECK_MESSAGE = "{org.hibernate.validator.constraints.ModCheck.message}";
    
    @RsmInfo(template = "不能为空", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_NOTBLANK_MESSAGE = "{org.hibernate.validator.constraints.NotBlank.message}";
    
    @RsmInfo(template = "不能为空", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_NOTEMPTY_MESSAGE = "{org.hibernate.validator.constraints.NotEmpty.message}";
    
    @RsmInfo(template = "执行脚本表达式\"{script}\"没有返回期望结果", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_PARAMETERSSCRIPTASSERT_MESSAGE = "{org.hibernate.validator.constraints.ParametersScriptAssert.message}";
    
    @RsmInfo(template = "需要在{min}和{max}之间", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_RANGE_MESSAGE = "{org.hibernate.validator.constraints.Range.message}";
    
    @RsmInfo(template = "执行脚本表达式\"{script}\"没有返回期望结果", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_SCRIPTASSERT_MESSAGE = "{org.hibernate.validator.constraints.ScriptAssert.message}";
    
    @RsmInfo(template = "需要是一个合法的URL", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_URL_MESSAGE = "{org.hibernate.validator.constraints.URL.message}";
    
    @RsmInfo(template = "必须小于${inclusive == true ? '或等于' : ''}${days == 0 ? '' : days += '天'}${hours == 0 ? '' : hours += '小时'}${minutes == 0 ? '' : minutes += '分钟'}${seconds == 0 ? '' : seconds += '秒'}${millis == 0 ? '' : millis += '毫秒'}${nanos == 0 ? '' : nanos += '纳秒'}", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_TIME_DURATIONMAX_MESSAGE = "{org.hibernate.validator.constraints.time.DurationMax.message}";
    
    @RsmInfo(template = "必须大于${inclusive == true ? '或等于' : ''}${days == 0 ? '' : days += '天'}${hours == 0 ? '' : hours += '小时'}${minutes == 0 ? '' : minutes += '分钟'}${seconds == 0 ? '' : seconds += '秒'}${millis == 0 ? '' : millis += '毫秒'}${nanos == 0 ? '' : nanos += '纳秒'}", status = HttpStatus.BAD_REQUEST)
    public static final String ORG_HIBERNATE_VALIDATOR_CONSTRAINTS_TIME_DURATIONMIN_MESSAGE = "{org.hibernate.validator.constraints.time.DurationMin.message}";
}
