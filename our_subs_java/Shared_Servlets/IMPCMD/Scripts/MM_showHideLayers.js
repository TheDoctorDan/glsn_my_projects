/* MM_showHideLayers  */

/* show hide list of layers */

function MM_showHideLayers() { 
    var i,p,v,obj,args=MM_showHideLayers.arguments;
    for (i=0; i<(args.length-2); i+=3)
        if ((obj=MM_findObj(args[i]))!=null) {
            v=args[i+2];
            if (obj.style) {
                obj=obj.style;
                v=(v=='show')?'visible':(v=='hide')?'hidden':v;
            }
            obj.visibility=v;
        }
}