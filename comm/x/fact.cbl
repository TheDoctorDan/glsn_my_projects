        identification division.
        program-id. factorials.
        
        data division.
        working-storage section.
        01 screen-data.
           05 display-number    pic ZZZZZ9.
           05 filler            pic XX value space.
           05 display-factorial pic ZZ,ZZZ,ZZZ,ZZ9.

        01 factorial-data.
           05 number    pic 9(4) comp.
           05 result    pic 9(9) comp.

        procedure division.
        start-para.
            display space upon crt.
            display "Number       Factorial"
            move 0 to number.
            perform calculate-para 12 times.
            display "Finished".
            stop  run.

        calculate-para.
            call "99" using number result.
            move number to display-number.
            move result to display-factorial.
            display screen-data.
            add 1 to number.
