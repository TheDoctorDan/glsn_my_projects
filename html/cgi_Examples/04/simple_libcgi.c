#include <stdio.h>
#include "cgi.h"

cgi_main (cgi_info *cgi)
{
    char *name,
         *age,
         *drink,
         *remote_host;

    form_entry *form_data;

    print_mimeheader ("text/plain");

    form_data = get_form_entries (cgi);
    name = parmval (form_data, "name");
    age = parmval (form_data, "age");
    drink = parmval (form_data, "drink");

    if (name == NULL) {
        printf ("Don't want to tell me your name, huh?\n");
        printf ("I know you are calling in from %s.\n\n", cgi->remote_host);
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
        printf ("You like: %s", drink);
        printf ("\n");
    }
    
    free_form_entries (form_data);
    exit(0);
}

