package nz.jovial.fopm.command.handling;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    public String name();
    
    public String usage() default "/<command>";
    
    public String description(); 
    
    public String aliases() default ""; //separated by commas, will convert later in handler.
    
    public PermLevel level() default PermLevel.OP;
    
    public SourceType source();
}
