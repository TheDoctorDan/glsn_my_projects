/* My_Escape */

/* escape any ' chars */

function My_Escape(s){

    answer="";

    for(i=0;i<s.length;i++){
        x=s.substring(i,i+1);
        if( x== '\'' )
            answer=answer+"\\";
        answer=answer+x;
    }
    return answer;
}