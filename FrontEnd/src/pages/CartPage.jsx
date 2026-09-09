import { useEffect, useState } from "react";
import { apiRequest } from "../api/httpClient.js";
import { StorefrontLayout } from "../layouts/StorefrontLayout.jsx";
import { ArrowRight, AlertCircle, RefreshCw, CheckCircle2, Package, Minus, Plus, Trash2 } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { getImageForProduct } from "../utils/imageMapper.js";

export function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [selectedIds, setSelectedIds] = useState([]);
  const [summary, setSummary] = useState({
    subTotal: 0,
    discountAmount: 0,
    shippingFee: 0,
    grandTotal: 0,
  });
  const [summaryLoading, setSummaryLoading] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [actionLoading, setActionLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = () => {
    setLoading(true);
    apiRequest("/api/v1/cart")
      .then((data) => {
        const items = data.items || [];
        setCartItems(items);
        setSelectedIds((prev) => {
          if (prev.length === 0) {
            return items.map((i) => i.productVariantId);
          }
          const validIds = items.map((i) => i.productVariantId);
          return prev.filter((id) => validIds.includes(id));
        });
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  };

  const toggleSelectItem = (variantId) => {
    setSelectedIds((prev) =>
      prev.includes(variantId)
        ? prev.filter((id) => id !== variantId)
        : [...prev, variantId]
    );
  };

  const toggleSelectAll = () => {
    if (selectedIds.length === cartItems.length) {
      setSelectedIds([]);
    } else {
      setSelectedIds(cartItems.map((item) => item.productVariantId));
    }
  };

  // Tính Order Summary thông qua API POST /api/v1/orders/summary mỗi khi selectedIds hoặc cartItems thay đổi
  useEffect(() => {
    const selectedItems = cartItems.filter((item) =>
      selectedIds.includes(item.productVariantId)
    );

    if (selectedItems.length === 0) {
      setSummary({
        subTotal: 0,
        discountAmount: 0,
        shippingFee: 0,
        grandTotal: 0,
      });
      return;
    }

    setSummaryLoading(true);
    apiRequest("/api/v1/orders/summary", {
      method: "POST",
      body: JSON.stringify({
        items: selectedItems.map((item) => ({
          productVariantId: item.productVariantId,
          quantity: item.quantity,
        })),
      }),
    })
      .then((res) => {
        setSummary({
          subTotal: Number(res.subTotal || 0),
          discountAmount: Number(res.discountAmount || 0),
          shippingFee: Number(res.shippingFee || 0),
          grandTotal: Number(res.grandTotal || 0),
        });
      })
      .catch((e) => {
        console.error("Lỗi khi tính order summary:", e);
      })
      .finally(() => setSummaryLoading(false));
  }, [selectedIds, cartItems]);

  const updateQuantity = async (variantId, newQuantity) => {
    if (newQuantity < 1) return;
    setActionLoading(true);
    try {
      await apiRequest(`/api/v1/cart/items`, {
        method: "PATCH",
        body: JSON.stringify({ productVariantId: variantId, quantity: newQuantity })
      });
      fetchCart();
    } catch (e) {
      alert("Lỗi: " + e.message + (e.payload ? " - " + JSON.stringify(e.payload) : ""));
    } finally {
      setActionLoading(false);
    }
  };

  const removeCartItem = async (variantId) => {
    setActionLoading(true);
    try {
      await apiRequest(`/api/v1/cart/items`, { 
        method: "PATCH",
        body: JSON.stringify({ productVariantId: variantId, quantity: 0 })
      });
      fetchCart();
    } catch (e) {
      alert("Lỗi: " + e.message + (e.payload ? " - " + JSON.stringify(e.payload) : ""));
    } finally {
      setActionLoading(false);
    }
  };

  const selectedCartItems = cartItems.filter((item) =>
    selectedIds.includes(item.productVariantId)
  );

  const selectedHasOutOfStock = selectedCartItems.some(
    (item) => item.stockStatus === "OUT_OF_STOCK"
  );

  const getStockStatusUI = (item) => {
    switch (item.stockStatus) {
      case "IN_STOCK":
        return (
          <div className="bg-[#e2f5e9] text-[#1e8b4e] text-[10px] font-black uppercase px-3 py-1 rounded-full tracking-wider flex items-center gap-1.5">
            <div className="w-1.5 h-1.5 rounded-full bg-[#1e8b4e]"></div> IN STOCK
          </div>
        );
      case "LIMITED":
        return (
          <div className="bg-[#fff8d6] text-[#b38600] text-[10px] font-black uppercase px-3 py-1 rounded-full tracking-wider flex items-center gap-1.5">
            <div className="w-1.5 h-1.5 rounded-full bg-[#b38600]"></div> LIMITED STOCK ({item.quantityInStock} LEFT)
          </div>
        );
      case "OUT_OF_STOCK":
      default:
        return (
          <div className="bg-[#fee3e1] text-[#c92a2a] text-[10px] font-black uppercase px-3 py-1 rounded-full tracking-wider flex items-center gap-1.5">
            <div className="w-1.5 h-1.5 rounded-full bg-[#c92a2a]"></div> OUT OF STOCK
          </div>
        );
    }
  };

  return (
    <StorefrontLayout>
      <div className="max-w-6xl mx-auto py-8">
        <h1 className="text-4xl font-black text-[#0c2b5e] mb-2 tracking-tight">Your Cart</h1>
        <p className="text-slate-500 font-semibold mb-8">Review inventory status and finalize your shipment request.</p>

        {loading && (
          <div className="py-20 flex justify-center text-[#1565c0]">
            <RefreshCw className="animate-spin" size={40} />
          </div>
        )}
        
        {error && (
          <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-center text-red-700 shadow-sm mb-8">
            <p className="font-bold mb-1">Could not load cart</p>
            <p className="text-sm">{error}</p>
          </div>
        )}

        {!loading && !error && (
          <div className="grid lg:grid-cols-12 gap-8 items-start">
            {/* Cart Items List */}
            <div className="lg:col-span-8 space-y-4">
              
              {selectedHasOutOfStock && (
                <div className="bg-[#fee8e7] border-l-4 border-[#c92a2a] rounded-lg p-5 flex items-start gap-3 shadow-sm mb-6">
                  <AlertCircle className="text-[#c92a2a] shrink-0 mt-0.5" size={20} />
                  <div>
                    <h3 className="text-[#961c1c] font-black text-sm">Validation Error</h3>
                    <p className="text-[#961c1c] font-medium text-sm mt-1 leading-snug">
                      Some items selected in your cart are currently out of stock. Please adjust quantities or unselect unavailable products to proceed.
                    </p>
                  </div>
                </div>
              )}

              {cartItems.length === 0 ? (
                <div className="flex flex-col items-center justify-center py-20 bg-white rounded-2xl border border-slate-200 text-slate-400">
                  <Package size={64} strokeWidth={1} className="mb-4 opacity-30" />
                  <p className="font-bold text-lg text-slate-600">Your cart is empty</p>
                  <button
                    onClick={() => navigate("/products")}
                    className="mt-6 bg-[#1565c0] text-white px-6 py-2.5 rounded-lg font-bold hover:bg-[#0d47a1] transition shadow-md"
                  >
                    Continue Shopping
                  </button>
                </div>
              ) : (
                <div className="space-y-4">
                  {/* Select All Bar */}
                  <div className="bg-white rounded-2xl border border-slate-200 p-4 flex items-center justify-between shadow-sm">
                    <label className="flex items-center gap-3 cursor-pointer select-none">
                      <input
                        type="checkbox"
                        checked={cartItems.length > 0 && selectedIds.length === cartItems.length}
                        onChange={toggleSelectAll}
                        className="w-5 h-5 accent-[#1565c0] rounded cursor-pointer"
                      />
                      <span className="font-bold text-slate-800 text-sm">
                        Chọn tất cả ({cartItems.length} sản phẩm)
                      </span>
                    </label>
                    <span className="text-xs font-semibold text-slate-500">
                      Đã chọn: <strong className="text-[#1565c0]">{selectedIds.length}</strong>/{cartItems.length}
                    </span>
                  </div>

                  {/* Cart Items */}
                  {cartItems.map((item) => {
                    const isOut = item.stockStatus === "OUT_OF_STOCK";
                    const isChecked = selectedIds.includes(item.productVariantId);

                    return (
                      <div
                        key={item.productVariantId}
                        className={`bg-white rounded-2xl border ${
                          isOut
                            ? "border-red-200 border-dashed bg-red-50/30"
                            : isChecked
                            ? "border-[#1565c0] ring-1 ring-[#1565c0]/20"
                            : "border-slate-200"
                        } p-5 flex flex-col sm:flex-row sm:items-center gap-4 sm:gap-6 shadow-sm transition-all hover:shadow-md relative overflow-hidden group`}
                      >
                        {/* Item Selection Checkbox */}
                        <div className="flex items-center shrink-0">
                          <input
                            type="checkbox"
                            checked={isChecked}
                            onChange={() => toggleSelectItem(item.productVariantId)}
                            className="w-5 h-5 accent-[#1565c0] rounded cursor-pointer shrink-0"
                          />
                        </div>

                        {/* Image Placeholder */}
                        <div className="w-24 h-24 rounded-xl bg-slate-100 flex items-center justify-center shrink-0 shadow-inner relative overflow-hidden">
                          <img
                            src={getImageForProduct(item.productName)}
                            alt={item.productName}
                            className="w-full h-full object-cover"
                          />
                          <div className="absolute inset-0 bg-gradient-to-tr from-black/10 to-transparent pointer-events-none"></div>
                        </div>

                        {/* Product Info */}
                        <div className="flex-1 min-w-0">
                          <h3 className="font-black text-slate-900 text-lg mb-1 truncate">{item.productName}</h3>
                          <p className="text-xs font-bold text-slate-500 uppercase tracking-widest mb-4">
                            SKU: {item.sku || "N/A"}
                          </p>

                          <div className="flex items-center gap-4">
                            {/* Quantity Controls */}
                            <div className="flex items-center bg-[#f1f5f9] rounded-lg border border-slate-200 p-1">
                              <button
                                onClick={() => updateQuantity(item.productVariantId, item.quantity - 1)}
                                disabled={actionLoading || item.quantity <= 1}
                                className="w-8 h-8 flex items-center justify-center text-slate-500 hover:text-slate-900 hover:bg-white rounded-md transition disabled:opacity-50"
                              >
                                <Minus size={14} strokeWidth={3} />
                              </button>
                              <span className="w-10 text-center font-black text-slate-900 text-sm">{item.quantity}</span>
                              <button
                                onClick={() => updateQuantity(item.productVariantId, item.quantity + 1)}
                                disabled={
                                  actionLoading ||
                                  (item.stockStatus !== "IN_STOCK" && item.quantity >= item.quantityInStock)
                                }
                                className="w-8 h-8 flex items-center justify-center text-slate-500 hover:text-slate-900 hover:bg-white rounded-md transition disabled:opacity-50"
                              >
                                <Plus size={14} strokeWidth={3} />
                              </button>
                            </div>

                            {/* Remove button */}
                            <button
                              onClick={() => removeCartItem(item.productVariantId)}
                              className="w-8 h-8 flex items-center justify-center text-red-400 hover:text-red-600 hover:bg-red-50 rounded-md transition"
                              title="Xoá sản phẩm"
                            >
                              <Trash2 size={16} />
                            </button>
                          </div>
                        </div>

                        {/* Price & Status */}
                        <div className="flex flex-col items-start sm:items-end gap-4 sm:gap-6 mt-4 sm:mt-0">
                          <div className="text-left sm:text-right">
                            <p className="font-black text-[#1565c0] text-xl">
                              {Number(item.price || 0).toLocaleString("vi-VN")}₫
                            </p>
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest mt-1">
                              Unit Price
                            </p>
                          </div>
                          {getStockStatusUI(item)}
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>

            {/* Order Summary Sidebar */}
            <div className="lg:col-span-4">
              <div className="bg-[#f8f9fc] rounded-3xl p-6 sm:p-8 sticky top-24">
                <div className="flex items-center justify-between mb-8">
                  <h2 className="text-2xl font-black text-slate-900">Order Summary</h2>
                  {summaryLoading && (
                    <RefreshCw className="animate-spin text-[#1565c0]" size={18} />
                  )}
                </div>

                <div className="space-y-4 mb-6">
                  <div className="flex justify-between items-center text-sm">
                    <span className="font-bold text-slate-600">Subtotal ({selectedCartItems.length} món)</span>
                    <span className="font-black text-slate-900">
                      {summary.subTotal.toLocaleString("vi-VN")}₫
                    </span>
                  </div>
                  {summary.discountAmount > 0 && (
                    <div className="flex justify-between items-center text-sm text-green-600">
                      <span className="font-bold">Giảm giá</span>
                      <span className="font-black">-{summary.discountAmount.toLocaleString("vi-VN")}₫</span>
                    </div>
                  )}
                  <div className="flex justify-between items-center text-sm">
                    <span className="font-bold text-slate-600">Expedited Shipping</span>
                    <span className="font-black text-slate-900">
                      {summary.shippingFee.toLocaleString("vi-VN")}₫
                    </span>
                  </div>
                </div>

                <div className="h-px bg-slate-200/60 mb-6"></div>

                <div className="flex justify-between items-end mb-8">
                  <span className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-1">
                    Total Amount
                  </span>
                  <div className="flex items-baseline gap-2">
                    <span className="text-3xl font-black text-[#1565c0]">
                      {summary.grandTotal.toLocaleString("vi-VN")}₫
                    </span>
                    <span className="text-[10px] font-black text-slate-500">VND</span>
                  </div>
                </div>

                <button
                  disabled={selectedCartItems.length === 0 || selectedHasOutOfStock || summaryLoading}
                  onClick={() =>
                    navigate("/checkout", {
                      state: {
                        items: selectedCartItems.map((item) => ({
                          productVariantId: item.productVariantId,
                          quantity: item.quantity,
                        })),
                        isBuyNow: false,
                      },
                    })
                  }
                  className="w-full flex items-center justify-center gap-2 bg-[#1a429b] text-white py-4 rounded-xl font-black hover:bg-[#123175] shadow-lg shadow-blue-900/20 transition disabled:opacity-50 disabled:shadow-none mb-4"
                >
                  Place Order ({selectedCartItems.length})
                  <ArrowRight size={18} />
                </button>

                <button
                  onClick={fetchCart}
                  disabled={loading}
                  className="w-full flex items-center justify-center gap-2 py-3 rounded-xl font-bold text-slate-600 hover:bg-slate-200/50 transition mb-6"
                >
                  <RefreshCw size={16} className={loading ? "animate-spin" : ""} />
                  Re-validate Inventory
                </button>

                <div className="bg-[#eef2f9] rounded-xl p-4 flex gap-3">
                  <CheckCircle2 className="text-[#1565c0] shrink-0 mt-0.5" size={18} />
                  <div>
                    <h4 className="text-[#1565c0] font-black text-[10px] uppercase tracking-widest mb-1">
                      Logistics Guarantee
                    </h4>
                    <p className="text-xs font-semibold text-slate-600 leading-relaxed">
                      Real-time inventory lock applied upon successful validation.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </StorefrontLayout>
  );
}
