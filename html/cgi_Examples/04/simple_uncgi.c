#include <stdio.h>
#include <stdlib.h>

void main (void)
{
  char*name,
         *age,
         *drink,
         *remote_host;

    printf ("Content-type: text/plain\n\n");
    
    uncgi();

    name = getenv ("WWW_name");
    age = getenv ("WWW_age");
    drink = getenv ("WWW_drink");
    remote_host = getenv ("REMOTE_HOST");

    if (name == NULL) {
        printf ("Don't want to tell me your name, huh?\n");
        printf ("I know you are calling in from %s.\n\n", remote_host);
    } else {
        printf ("Hi %s -- Nice to meet you.\n", name);
    }
    
    if (age == NULL) {
        printf ("Are you shy about your age?\n");
    } else {
        printf ("You are %s years old.\n", age);
    }
    
    printf ("\n");

    if (drink == NULL) {
        printf ("I guess you don't like any fluids.\n");
    } else {
        printf ("You like: ");
        
        while (*drink != '\0') {
	  if (*drink == '#') {
                printf (" ");
	  } else {
                printf ("%c", *drink);
	  }
            ++drink;
        }
    
        printf ("\n");
    }
    
    exit(0);
}
