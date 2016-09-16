#include "all.h"
/* eval.c 42d */
static Valuelist evallist(Explist es, Valenv globals, Funenv functions, Valenv
                                                                       formals, Valenv locals){
    if(es == NULL)
        return NULL;
    else{
        Value val = eval(es->hd, globals,functions,formals,locals);
        return mkVL(val, evallist(es->tl,globals,functions,formals,locals));
    }
}
Valuelist locallist(int temp, Valuelist val){
    if(temp>1){
        val = mkVL(0,val);
        return locallist(--temp,val);
    }
    return mkVL(0,val);
}
/* eval.c 43a */
Value eval(Exp e, Valenv globals, Funenv functions, Valenv formals,Valenv locals) {
    checkoverflow(1000000 * sizeof(char *));
                                  
    switch (e->alt) {
    case LITERAL:
        /* evaluate [[e->u.literal]] and return the result 43b */

        return e->u.literal;
    case VAR:
        /* evaluate [[e->u.var]] and return the result 44a */
        if(isvalbound(e->u.var,locals))
            return fetchval(e->u.var,locals);
        if (isvalbound(e->u.var, formals))
            return fetchval(e->u.var, formals);
        else if (isvalbound(e->u.var, globals))
            return fetchval(e->u.var, globals);
        else
            runerror("unbound variable %n", e->u.var);
        assert(0);  
    case SET:
        /* evaluate [[e->u.set]] and return the result 44b */
        {
            Value v = eval(e->u.set.exp, globals, functions, formals,locals);
            if(isvalbound(e->u.set.name,locals))
                bindval(e->u.set.name,v,locals);
            if (isvalbound(e->u.set.name, formals))
                bindval(e->u.set.name, v, formals);
            else if (isvalbound(e->u.set.name, globals))
                bindval(e->u.set.name, v, globals);
            else
                runerror("tried to set unbound variable %n in %e", e->u.set.name
                                                                           , e);
            return v;
        }
    case IFX:
        /* evaluate [[e->u.ifx]] and return the result 45a */
        if (eval(e->u.ifx.cond, globals, functions, formals,locals) != 0)
            return eval(e->u.ifx.true, globals, functions, formals,locals);
        else
            return eval(e->u.ifx.false, globals, functions, formals,locals);
    case WHILEX:
        /* evaluate [[e->u.whilex]] and return the result 45b */
        while (eval(e->u.whilex.cond, globals, functions, formals,locals) != 0)
            eval(e->u.whilex.exp, globals, functions, formals,locals);
        return 0;
    case BEGIN:
        /* evaluate [[e->u.begin]] and return the result 46a */
        {
            Value lastval = 0;
            Explist es;
            for ( es = e->u.begin; es; es = es->tl)
                lastval = eval(es->hd, globals, functions, formals,locals);
            return lastval;
        }
    case APPLY:
        /* evaluate [[e->u.apply]] and return the result 46b */
        {
            Fun f;

/* make [[f]] the function denoted by [[e->u.apply.name]], or call [[runerror]] 46c */
            if (!isfunbound(e->u.apply.name, functions))
                runerror("call to undefined function %n in %e", e->u.apply.name,
                                                                             e);
            f = fetchfun(e->u.apply.name, functions);
            switch (f.alt) {
            case USERDEF:
                /* apply [[f.u.userdef]] and return the result 47b */
                {
                    Namelist  xs = f.u.userdef.formals;
                    Valuelist vs = evallist(e->u.apply.actuals, globals,
                                                            functions, formals,locals);
                    Namelist local_xs = f.u.userdef.locals;
                    Valuelist local_vs = locallist(lengthNL(f.u.userdef.locals),NULL);
                    checkargc(e, lengthNL(xs), lengthVL(vs));
                    return eval(f.u.userdef.body, globals, functions, mkValenv(xs,vs),mkValenv(
                                                                       local_xs,local_vs));
                }
            case PRIMITIVE:
                /* apply [[f.u.primitive]] and return the result 48a */
                {
                    Valuelist vs = evallist(e->u.apply.actuals, globals,
                                                            functions, formals,locals);
                    if (f.u.primitive == strtoname("print"))

              /* apply \impcore\ primitive [[print]] to [[vs]] and return 48b */
                        {
                            checkargc(e, 1, lengthVL(vs));
                            Value v = nthVL(vs, 0);
                            print("%v", v);
                            return v;
                        }
                    else if (f.u.primitive == strtoname("println"))

            /* apply \impcore\ primitive [[println]] to [[vs]] and return 48c */
                        {
                            checkargc(e, 1, lengthVL(vs));
                            Value v = nthVL(vs, 0);
                            print("%v\n", v);
                            return v;
                        }
                    else if (f.u.primitive == strtoname("printu"))

             /* apply \impcore\ primitive [[printu]] to [[vs]] and return 48d */
                        {
                            checkargc(e, 1, lengthVL(vs));
                            Value v = nthVL(vs, 0);
                            print_utf8(v);
                            return v;
                        }
                    else

                       /* apply arithmetic primitive to [[vs]] and return 49a */
                        {
                            const char *s = nametostr(f.u.primitive);
                            Value v, w;

/* check that [[vs]] has exactly two values, and assign them to [[v]] and [[w]] 49b */
                            checkargc(e, 2, lengthVL(vs));
                            v = nthVL(vs, 0);
                            w = nthVL(vs, 1);
                            assert(strlen(s) == 1);

                            switch (s[0]) {
                            case '<':
                                return v < w;
                            case '>':
                                return v > w;
                            case '=':
                                return v == w;
                            case '+':
                                return v + w;
                            case '-':
                                return v - w;
                            case '*':
                                return v * w;
                            case '/':
                                if (w == 0)
                                    runerror("division by zero in %e", e);
                                return v / w;
                            default:
                                assert(0);
                            }
                        }
                }
            default:
                assert(0);
            }
        }
    }
    assert(0);
}
/* eval.c 50a */
void readevalprint(XDefstream xdefs, Valenv globals, Funenv functions, Echo echo
                                                                             ) {
    UnitTestlist pending_unit_tests = NULL;
                                            // to be run when xdefs is exhausted

    for (XDef d = getxdef(xdefs); d; d = getxdef(xdefs))
        switch (d->alt) {
        case TEST:
            pending_unit_tests = mkUL(d->u.test, pending_unit_tests);
            break;
        case USE:

/* evaluate [[d->u.use]], possibly mutating [[globals]] and [[functions]] 50c */
            {
                const char *filename = nametostr(d->u.use);
                FILE *fin = fopen(filename, "r");
                if (fin == NULL)
                    runerror("cannot open file \"%s\"", filename);
                readevalprint(filexdefs(filename, fin, NO_PROMPTS), globals,
                                                               functions, echo);
                fclose(fin);
            }
            break;
        case DEF:
            evaldef(d->u.def, globals, functions, echo);
            break;
        default:
            assert(0);
        }

    process_tests(pending_unit_tests, globals, functions);
}
/* eval.c 51a */
void evaldef(Def d, Valenv globals, Funenv functions, Echo echo) {
    switch (d->alt) {
    case VAL:
        /* evaluate [[d->u.val]], mutating [[globals]] 48b */
        {
            Value v = eval(d->u.val.exp, globals, functions, 
                           mkValenv(NULL, NULL), mkValenv(NULL, NULL));
            bindval(d->u.val.name, v, globals);
            if (echo == ECHOING)
                print("%v\n", v);
        }
        return;
    case EXP:
        /* evaluate [[d->u.exp]] and possibly print the result 48c */
        {
            Value v = eval(d->u.exp, globals, functions, 
                           mkValenv(NULL, NULL), mkValenv(NULL, NULL));
            bindval(strtoname("it"), v, globals);
            if (echo == ECHOING)
                print("%v\n", v);
        }
        return;
    case DEFINE:
        /* evaluate [[d->u.define]], mutating [[functions]] 49a */
        /* fail if [[d->u.define]] has duplicate formal parameters 49b */
        if (duplicatename(d->u.define.userfun.formals) != NULL)
            error(
         "Formal parameter named %n appears twice in definition of function %n",
                  duplicatename(d->u.define.userfun.formals), d->u.define.name);

        if (duplicatename(d->u.define.userfun.locals) != NULL)
            error(
         "Local parameter named %n appears twice in definition of function %n",
                  duplicatename(d->u.define.userfun.locals), d->u.define.name);


        bindfun(d->u.define.name, mkUserdef(d->u.define.userfun), functions);
        if (echo == ECHOING)
            print("%n\n", d->u.define.name);
        return;
    }
    assert(0);
}
