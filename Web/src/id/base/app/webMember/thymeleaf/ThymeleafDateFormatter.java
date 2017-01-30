package id.base.app.webMember.thymeleaf;

import id.base.app.SystemConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

public class ThymeleafDateFormatter implements Formatter<Date>{
	@Autowired
    private MessageSource messageSource;


    public ThymeleafDateFormatter() {
        super();
    }

    public Date parse(final String text, final Locale locale) throws ParseException {
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.parse(text);
    }

    public String print(final Date object, final Locale locale) {
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.format(object);
    }

    private SimpleDateFormat createDateFormat(final Locale locale) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(SystemConstant.SYSTEM_DATE_MASK);
        dateFormat.setLenient(false);
        return dateFormat;
    }
}
