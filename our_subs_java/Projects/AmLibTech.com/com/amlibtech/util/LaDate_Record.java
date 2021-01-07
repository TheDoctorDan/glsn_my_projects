/* LaDate_Record.java */
/**
*
* Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004
* American Liberator Technologies
* All Rights Reserved
*
* THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
* American Liberator Technologies
* The copyright notice above does not evidence any
* actual or intended publication of such source code.
*
**/

package com.amlibtech.util;

public class LaDate_Record {
	/**
	* these values all start at zero and go to max of
	* 99,99,3,11,30,365,36524,6,30,365  respectively
	**/
	private int	century, year, quarter_of_year, month_of_year,
		day_of_month, day_of_year, day_of_century, day_of_week,
		last_day_this_month, last_day_this_year;
	/**
	* original LaDate Value
	**/
	private LaDate	ladate;

    private static final String[] Copyright= {
    "|       Copyright (c) 1985 thru 2001, 2002, 2003, 2004, 2005, 2006, 2007       |",
    "|       American Liberator Technologies                                        |",
    "|       All Rights Reserved                                                    |",
    "|                                                                              |",
    "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
    "|       American Liberator Technologies                                        |",
    "|       The copyright notice above does not evidence any                       |",
    "|       actual or intended publication of such source code.                    |"
    };

	public LaDate_Record(LaDate passed_ladate){
		long	century_leap_days, year_leap_days;
		int	i;
		boolean	leap_year_flag;
		boolean	do_century_loop, do_year_loop, do_century_again;
		int[]	days = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
		int[]	days_per_month = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		/* zero all values */
		this.century= this.year= this.quarter_of_year= this.month_of_year=
		this.day_of_month= this.day_of_year= this.day_of_century= this.day_of_week=
		this.last_day_this_month= this.last_day_this_year= 0;

		this.ladate=passed_ladate;

                
		this.day_of_week=(int)((passed_ladate.value + 6) % 7);
                


		if(passed_ladate.value >=0){
			this.century=(int)(passed_ladate.value / 36524);

			do_century_again=true;
			while(do_century_again){
				do_century_again=false;

				do_century_loop=true;
//century_loop:
				while(do_century_loop) {
					this.day_of_century=(int)(passed_ladate.value  - (long)this.century * 36524);
					century_leap_days=(this.century+3)/4;
					this.day_of_century-=century_leap_days;

					if(this.day_of_century<0){
						this.century-=1;
					}else{
						do_century_loop=false;
					}
				}

				this.year=this.day_of_century/365;
				do_year_loop=true;
//year_loop:
				while(do_year_loop){
					this.day_of_year=this.day_of_century - this.year*365;
					year_leap_days=Math.abs((this.year-1)/4);
					if(this.century %4 ==0 && this.year>0)
						year_leap_days++;
					this.day_of_year-=year_leap_days;
					if(this.day_of_year<0){
						this.year-=1;
						if(this.year<0){
							this.century--;
							do_century_loop=true;
							do_year_loop=false;
						}else {
							do_year_loop=true;
						}
					}else{
						do_year_loop=false;
					}
				}
			}


			leap_year_flag=false;
			if(this.year==0){
				if(this.century%4==0)
					leap_year_flag=true;
			}else if(this.year%4==0)
				leap_year_flag=true;
			for(i=1;i<12;i++){
				if(leap_year_flag){
					if(i>1){
						if(this.day_of_year <days[i]+1)
							break;
					}else{
						if(this.day_of_year <days[i])
							break;
					}
				}else{
					if(this.day_of_year <days[i])
						break;
				}
			}
			this.month_of_year=i-1;
			this.quarter_of_year= this.month_of_year/3;
			this.day_of_month=this.day_of_year - days[this.month_of_year];
			this.last_day_this_month=days_per_month[this.month_of_year]-1;
			if(leap_year_flag){
				if(this.month_of_year>1)
					this.day_of_month--;
				if(this.month_of_year==1)
					this.last_day_this_month=28;
			}
			if(leap_year_flag)
				this.last_day_this_year=365;
			else
				this.last_day_this_year=364;

		}
	}
        
        
       
        
        /**
         * Getter for property ladate.
         * @return Value of property ladate.
         */
        public com.amlibtech.util.LaDate getLadate() {
            return ladate;
        }
        
        /**
         * Getter for property century.
         * @return Value of property century.
         */
        public int getCentury() {
            return century;
        }        
        
              
        
        /**
         * Getter for property year.
         * @return Value of property year.
         */
        public int getYear() {
            return year;
        }        
        
        
        
        /**
         * Getter for property quarter_of_year.
         * @return Value of property quarter_of_year.
         */
        public int getQuarter_of_year() {
            return quarter_of_year;
        }
        
        
        
        /**
         * Getter for property month_of_year.
         * @return Value of property month_of_year.
         */
        public int getMonth_of_year() {
            return month_of_year;
        }
        
        
        /**
         * Getter for property day_of_month.
         * @return Value of property day_of_month.
         */
        public int getDay_of_month() {
            return day_of_month;
        }
        
        
        
        /**
         * Getter for property day_of_year.
         * @return Value of property day_of_year.
         */
        public int getDay_of_year() {
            return day_of_year;
        }
        
        
        
        /**
         * Getter for property day_of_century.
         * @return Value of property day_of_century.
         */
        public int getDay_of_century() {
            return day_of_century;
        }
        
        
        
        /**
         * Getter for property day_of_week.
         * @return Value of property day_of_week.
         */
        public int getDay_of_week() {
            return day_of_week;
        }
        
        
        
        /**
         * Getter for property last_day_this_month.
         * @return Value of property last_day_this_month.
         */
        public int getLast_day_this_month() {
            return last_day_this_month;
        }
        
        
        
        /**
         * Getter for property last_day_this_year.
         * @return Value of property last_day_this_year.
         */
        public int getLast_day_this_year() {
            return last_day_this_year;
        }
        
        
        
}
