exception Mismatch
fun zip ([], []) = []
  | zip (x::xs, y::ys) = (x,y)::zip(xs,ys)
  | zip (nil, y::ys) = raise Mismatch
  | zip (x::xs, nil) = raise Mismatch;
